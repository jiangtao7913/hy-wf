package com.hy.wf.api.dao.base;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jt
 */
@Slf4j
public class UpdateBuilder<T, K> {
    private final BaseRepositoryImpl<T, K> repository;

    private final List<FieldValue> fieldValueList = new ArrayList<>();
    private final ConditionBuilder conditionBuilder = new ConditionBuilder();
    private int limit;

    private final List<Object> args = new ArrayList<>();

    UpdateBuilder(BaseRepositoryImpl<T, K> repository) {
        this.repository = repository;
    }

    public UpdateBuilder<T, K> set(String field, Object value) {
        fieldValueList.add(new FieldValue(field, false, value));
        return this;
    }

    public UpdateBuilder<T, K> setRawField(String raw) {
        fieldValueList.add(new FieldValue(raw, true, null));
        return this;
    }

    public UpdateBuilder<T, K> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public UpdateBuilder<T, K> where(String where, Object... args) {
        conditionBuilder.where(where, args);
        return this;
    }

    public UpdateBuilder<T, K> where(Condition condition) {
        conditionBuilder.where(condition);
        return this;
    }

    public int update() {
        return repository.update(getSql(), getArgs());
    }

    private CharSequence getSql() {
        Preconditions.checkArgument(!fieldValueList.isEmpty());

        args.clear();
        StringBuilder sql = new StringBuilder("UPDATE ").append(repository.getTableName()).append(" SET");
        boolean fieldFirst = true;
        for (FieldValue fieldValue : fieldValueList) {
            if (!fieldFirst) {
                sql.append(",");
            }
            if (fieldValue.isRaw()) {
                sql.append(" ").append(fieldValue.getField());
            } else {
                sql.append(" ").append(fieldValue.getField()).append(" = ?");
                args.add(fieldValue.value);
            }
            fieldFirst = false;
        }

        if (conditionBuilder.hasCondition()) {
            final Condition condition = conditionBuilder.getCondition();
            sql.append(" WHERE ");
            sql.append(condition.getSql());
            Collections.addAll(args, condition.getArgs());
        } else {
            throw new IllegalStateException("update data but not exists condition");
        }


        if (limit > 0) {
            sql.append(" LIMIT ?");
            args.add(limit);
        }

        log.debug("sql: {}", sql);

        return sql;
    }

    private Object[] getArgs() {
        return args.toArray();
    }

    @Data
    private class FieldValue {
        private final String field;
        private final boolean raw;
        private final Object value;
    }
}
