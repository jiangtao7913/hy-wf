package com.hy.wf.api.dao.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ComplexCondition implements Condition {

    private final Condition first;
    private final boolean or;
    private final Condition second;

    private String sql;
    private final List<Object> args = new ArrayList<>();

    ComplexCondition(Condition first, boolean or, Condition second) {
        this.first = first;
        this.or = or;
        this.second = second;
    }


    @Override
    public String getSql() {
        if (sql == null) {
            final String firstSql = first.getSql();
            final Object[] firstArgs = first.getArgs();

            final String secondSql = second.getSql();
            final Object[] secondArgs = second.getArgs();

            StringBuilder builder = new StringBuilder(firstSql.length() + secondArgs.length + 10);

            builder.append("(");

            builder.append(firstSql);
            if (or) {
                builder.append(" OR ");
            } else {
                builder.append(" AND ");
            }
            builder.append(secondSql);

            builder.append(")");
            sql = builder.toString();

            Collections.addAll(args, firstArgs);
            Collections.addAll(args, secondArgs);

        }
        return sql;
    }

    @Override
    public Object[] getArgs() {
        return args.toArray();
    }

    @Override
    public Condition or(Condition condition) {
        return ComplexCondition.or(this, condition);
    }

    @Override
    public Condition and(Condition condition) {
        return ComplexCondition.and(this, condition);
    }

    static ComplexCondition or(Condition first, Condition second) {
        return new ComplexCondition(first, true, second);
    }

    static ComplexCondition and(Condition first, Condition second) {
        return new ComplexCondition(first, false, second);
    }
}