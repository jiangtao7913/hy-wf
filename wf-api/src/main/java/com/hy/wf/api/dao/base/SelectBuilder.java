package com.hy.wf.api.dao.base;

import com.google.common.base.Preconditions;
import com.hy.wf.common.DateUtils;
import com.hy.wf.entity.base.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jt
 */
@Slf4j
public class SelectBuilder<T, K> {
    private final List<String> fields = new ArrayList<>();
    private final List<String> sorts = new ArrayList<>();
    private final ConditionBuilder conditionBuilder = new ConditionBuilder();

    private final List<Object> args = new ArrayList<>();

    private int offset;
    private int limit;

    private boolean lock = false;

    private final BaseRepositoryImpl<T, K> repository;

    SelectBuilder(BaseRepositoryImpl<T, K> repository) {
        this.repository = repository;
    }

    public SelectBuilder<T, K> field(String... fields) {
        Preconditions.checkNotNull(fields);
        Collections.addAll(this.fields, fields);
        return this;
    }

    public SelectBuilder<T, K> where(String where, Object... args) {
        conditionBuilder.where(where, args);
        return this;
    }

    public SelectBuilder<T, K> where(Condition condition) {
        conditionBuilder.where(condition);
        return this;
    }

    public SelectBuilder<T, K> comm(){
        conditionBuilder.where(Condition.equal("data_status", BaseEntity.DataStatus.valid.value));
        return this;
    }

    public SelectBuilder<T, K> date(String startColumn, String endColumn, Date startDate, Date endDate){
        conditionBuilder.where("DATE_FORMAT(?,'%Y-%m-%d') >= DATE_FORMAT("+startColumn+",'%Y-%m-%d')" +
                        "AND  DATE_FORMAT(?,'%Y-%m-%d')<=  DATE_FORMAT("+endColumn+",'%Y-%m-%d')",
                DateUtils.format(startDate,"yyyy-MM-dd HH:mm:ss"),DateUtils.format(endDate,"yyyy-MM-dd HH:mm:ss"));
        return this;
    }


    public SelectBuilder<T, K> lock(boolean lock) {
        this.lock = lock;
        return this;
    }

    public SelectBuilder<T, K> offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SelectBuilder<T, K> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectBuilder<T, K> orderBy(String field, boolean asc) {
        this.sorts.add(field + (asc ? " asc" : " desc"));
        return this;
    }

    public SelectBuilder<T, K> orderBy(String field) {
        this.sorts.add(field);
        return this;
    }


    public T findOne() {
        this.limit(1);
        final CharSequence sql = getSql();
        return repository.findOne(sql, getArgs());
    }


    public List<T> find() {
        final CharSequence sql = getSql();
        return repository.find(sql, getArgs());
    }

    public int findCount() {
        List<String> origFields = new ArrayList<>(this.fields);
        fields.clear();
        fields.add("COUNT(*)");
        final int originLimit = limit;
        this.limit(0);
        final CharSequence sql = getSql();
        try {
            return repository.findCount(sql, getArgs());
        } finally {
            this.fields.clear();
            this.fields.addAll(origFields);
            this.limit(originLimit);
        }
    }

    public Page<T> page(int pageNo, int pageSize) {
        Preconditions.checkArgument(pageNo > 0, "page no must be greater then 0");

        final int count = this.findCount();
        final int pageOffset = pageSize * (pageNo - 1);
        final int originOffset = offset;
        final int originLimit = limit;
        try {
            this.offset(pageOffset);
            this.limit(pageSize);
            final List<T> data = find();
            return new Page<>(count, pageSize, pageNo, data);
        } finally {
            this.offset = originOffset;
            this.limit = originLimit;
        }
    }

    // ----

    private CharSequence getSql() {
        args.clear();
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.fields.isEmpty()) {
            sql.append(" *");
        } else {
            sql.append(this.fields.stream().collect(Collectors.joining(",")));
        }
        sql.append(" FROM ").append(repository.getTableName());

        if (conditionBuilder.hasCondition()) {
            final Condition condition = conditionBuilder.getCondition();
            sql.append(" WHERE ");
            sql.append(condition.getSql());
            Collections.addAll(args, condition.getArgs());
        }

        if (!sorts.isEmpty()) {
            sql.append(" ORDER BY ");
            final String orders = this.sorts.stream().collect(Collectors.joining(","));
            sql.append(orders);
        }

        if (limit > 0) {
            if (offset > 0) {
                sql.append(" LIMIT ?, ?");
                args.add(offset);
                args.add(limit);
            } else {
                sql.append(" LIMIT ?");
                args.add(limit);
            }
        }

        if(lock){
            sql.append(" for update");
        }

        log.debug("sql: {}", sql);

        return sql;
    }

    private Object[] getArgs() {
        return args.toArray();
    }


}