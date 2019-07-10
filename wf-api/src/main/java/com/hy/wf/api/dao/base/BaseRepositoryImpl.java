package com.hy.wf.api.dao.base;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jt
 */
@Slf4j
public abstract class BaseRepositoryImpl<T, K> implements BaseRepository<T, K> {
    @Resource
    protected JdbcTemplate jdbcTemplate;

    private final String tableName;

    @Override
    public void batchSave(List<T> list) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append("(");
        try {
            Converter<String, String> convert = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
            for (PropertyDescriptor d : descriptorList) {
                String name = convert.convert(d.getName());
                sqlBuilder.append(name).append(",");
            }
            List<Object[]> args = new ArrayList<>();
            for(T t :list){
                List<Object> arg = new ArrayList<>();
                for (PropertyDescriptor d : descriptorList) {
                   arg.add(d.getReadMethod().invoke(t));
                }
                Object[] a = arg.toArray();
                args.add(a);
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');
            sqlBuilder.append(" VALUES (");
            for (Object d : descriptorList) {
                sqlBuilder.append("?,");
            }

            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');
            jdbcTemplate.batchUpdate(sqlBuilder.toString(), args);

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void batchUpdate(List<T> list) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE  ").append(tableName).append(" SET ");
        try {
            Converter<String, String> convert = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
            for (PropertyDescriptor d : descriptorList) {
                String name = convert.convert(d.getName());
                if(!name.equals(idColumnName)){
                    sqlBuilder.append(name).append(" = ? ,");
                }
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ' ');
            sqlBuilder.append("where ").append(idColumnName).append(" = ?");
            List<Object[]> args = new ArrayList<>();
            for (T t : list) {
                List<Object> arg = new ArrayList<>();
                Long id = 0L;
                for (PropertyDescriptor d : descriptorList) {
                    if(d.getName().equals(idColumnName)){
                        id = (Long)d.getReadMethod().invoke(t);
                        continue;
                    }
                    arg.add(d.getReadMethod().invoke(t));
                }
                arg.add(id);
                Object[] a = arg.toArray();
                args.add(a);
            }
            jdbcTemplate.batchUpdate(sqlBuilder.toString(), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private final String idColumnName;
    private final BeanPropertyRowMapper<T> rowMapper;

    private final List<PropertyDescriptor> descriptorList;

    public BaseRepositoryImpl() {
        tableName = getTableName();
        idColumnName = getIdColumnName();
        final Class<T> beanClass = getBeanClass();
        rowMapper = new BeanPropertyRowMapper<>(beanClass);

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

            descriptorList = Arrays.stream(descriptors).filter(d -> d.getWriteMethod() != null).filter(d -> d.getReadMethod() != null)
                    .collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public T findById(K id) {
        return select().where(Condition.equal(idColumnName, id)).comm().findOne();
    }

    @Override
    public List<T> findInIds(Collection<? extends K> ids) {
        if (ids == null || ids.isEmpty()) {
            return Lists.newArrayList();
        }
        return select().where(Condition.in(idColumnName, ids.toArray())).comm().find();
    }

    @Override
    public List<T> findNotInIds(Collection<? extends K> ids) {
        if (ids == null || ids.isEmpty()) {
            return Lists.newArrayList();
        }
        return select().where(Condition.notIn(idColumnName, ids.toArray())).comm().find();
    }

    @Override
    public int deleteById(K id) {
        return delete().where(Condition.equal(idColumnName, id)).delete();
    }

    @Override
    public int totalCount() {
        return select().findCount();
    }

    @Override
    public int save(T t, KeyHolder keyHolder) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append("(");

        try {
            Converter<String, String> convert = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);

            List<Object> args = Lists.newArrayList();

            for (PropertyDescriptor d : descriptorList) {
                String name = convert.convert(d.getName());
                if (keyHolder == null) {
                    if (idColumnName.equals(name)) {
                        continue;
                    }
                }
                sqlBuilder.append(name).append(",");
                args.add(d.getReadMethod().invoke(t));
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

            sqlBuilder.append(" VALUES (");
            for (Object arg : args) {
                sqlBuilder.append("?,");
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

            if (keyHolder == null) {
                return jdbcTemplate.update(sqlBuilder.toString(), args.toArray());
            } else {
                log.debug("sql: {}", sqlBuilder);
                PreparedStatementCreator psc = connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
                    int i = 1;
                    for (Object arg : args) {
                        preparedStatement.setObject(i++, arg);
                    }
                    return preparedStatement;
                };
                return jdbcTemplate.update(psc, keyHolder);
            }


        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findAll() {
        return select().find();
    }

    // --------------------------------

    public int findCount(CharSequence sql, Object... args) {
        return jdbcTemplate.query(sql.toString(), args, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }


    public List<T> find(CharSequence sql, Object... args) {
        return jdbcTemplate.query(sql.toString(), args, rowMapper);
    }

    public T findOne(CharSequence sql, Object... args) {
        return jdbcTemplate.query(sql.toString(), args, rs -> {
        if (rs.next()) {
            return rowMapper.mapRow(rs, 0);
        }
        return null;

    });
}

    public int update(CharSequence sql, Object... args) {
        return jdbcTemplate.update(sql.toString(), args);
    }

    protected String getIdColumnName() {
        return "id";
    }


    public abstract String getTableName();

    public abstract Class<T> getBeanClass();

    // ------------------------------- protected methods

    protected SelectBuilder<T, K> select() {
        return new SelectBuilder<>(this);
    }

    protected UpdateBuilder<T, K> update() {
        return new UpdateBuilder<>(this);
    }

    protected DeleteBuilder<T, K> delete() {
        return new DeleteBuilder<>(this);
    }
}
