package com.hy.wf.api.dao.base;

import lombok.Getter;

import java.util.Objects;

/**
 * @author jt
 */
final class ConditionBuilder {
    @Getter
    private Condition condition;

    protected void where(String where, Object... args) {
        final RawCondition condition = new RawCondition(where, args);
        this.condition = Objects.isNull(this.condition) ? condition : this.condition.and(condition);
    }

    protected void where(Condition condition) {
        this.condition = Objects.isNull(this.condition) ? condition : this.condition.and(condition);
    }

    protected boolean hasCondition() {
        return condition != null;
    }
}
