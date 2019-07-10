package com.hy.wf.api.dao.base;


import java.util.Collection;

/**
 * @author jt
 */
public interface Condition {

    String getSql();

    Object[] getArgs();

    Condition or(Condition condition);

    Condition and(Condition condition);


    static Condition equal(String field, Object arg) {
        return new SimpleCondition(field, Opt.EQ, arg);
    }

    static Condition lessThen(String field, Object arg) {
        return new SimpleCondition(field, Opt.LT, arg);
    }

    static Condition greaterThen(String field, Object arg) {
        return new SimpleCondition(field, Opt.GT, arg);
    }

    static Condition lessThanEqual(String field, Object arg) {
        return new SimpleCondition(field, Opt.LTE, arg);
    }

    static Condition greaterThanEqual(String field, Object arg) {
        return new SimpleCondition(field, Opt.GTE, arg);
    }

    static Condition in(String field, Object[] args) {
        return new SimpleCondition(field, Opt.IN, args);
    }

    static Condition in(String field, Collection<?> args) {
        return in(field, args.toArray());
    }

    static Condition notIn(String field, Object[] args) {
        return new SimpleCondition(field, Opt.NOT_IN, args);
    }

    static Condition notIn(String field, Collection<?> args) {
        return notIn(field, args.toArray());
    }


}
