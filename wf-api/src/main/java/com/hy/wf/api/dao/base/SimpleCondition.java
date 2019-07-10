package com.hy.wf.api.dao.base;

import java.util.ArrayList;
import java.util.List;

public class SimpleCondition implements Condition {
    private final String field;
    private final Opt opt;
    private final Object arg;

    private String sql;
    private final List<Object> args = new ArrayList<>();

    public SimpleCondition(String field, Opt opt, Object arg) {
        this.field = field;
        this.opt = opt;
        this.arg = arg;
    }


    @Override
    public String getSql() {
        if (sql == null) {
            args.clear();
            final StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(field).append(" ").append(opt.symbol).append(" ");
            if (opt != Opt.IN && opt != Opt.NOT_IN) {
                sqlBuilder.append("?");
                args.add(arg);
            } else {
                sqlBuilder.append("(");
                boolean first = true;
                for (Object o : ((Object[]) arg)) {
                    if (!first) {
                        sqlBuilder.append(",");
                    } else {
                        first = false;
                    }
                    sqlBuilder.append("?");
                    args.add(o);
                }
                sqlBuilder.append(")");
            }

            this.sql = sqlBuilder.toString();
        }
        return sql;
    }

    @Override
    public Object[] getArgs() {
        getSql();
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
}




