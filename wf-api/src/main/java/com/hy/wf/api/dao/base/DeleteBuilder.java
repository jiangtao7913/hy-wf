package com.hy.wf.api.dao.base;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DeleteBuilder<T, K> {
    private final ConditionBuilder conditionBuilder = new ConditionBuilder();
    private final BaseRepositoryImpl<T, K> repository;
    private final List<String> sorts = new ArrayList<>();

    private final List<Object> args = new ArrayList<>();

    private int offset;
    private int limit;

    public DeleteBuilder(BaseRepositoryImpl<T, K> repository) {
        this.repository = repository;
    }

    public DeleteBuilder<T, K> orderBy(String field, boolean asc) {
        this.sorts.add(field + (asc ? " asc" : " desc"));
        return this;
    }

    public DeleteBuilder<T, K> orderBy(String field) {
        this.sorts.add(field);
        return this;
    }

    public DeleteBuilder<T, K> offset(int offset) {
        this.offset = offset;
        return this;
    }

    public DeleteBuilder<T, K> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public DeleteBuilder<T, K> where(String where, Object... args) {
        conditionBuilder.where(where, args);
        return this;
    }

    public DeleteBuilder<T, K> where(Condition condition) {
        conditionBuilder.where(condition);
        return this;
    }

    public int delete() {
        return repository.update(getSql(), getArgs());
    }

    private CharSequence getSql() {
        args.clear();
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(repository.getTableName());

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

        log.debug("sql: {}", sql);

        return sql;
    }

    private Object[] getArgs() {
        return args.toArray();
    }

}
