package com.hy.wf.api.dao.base;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
class RawCondition implements Condition {

    private final String condition;
    @Getter
    private final Object[] args;

    @Override
    public String getSql() {
        return condition;
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