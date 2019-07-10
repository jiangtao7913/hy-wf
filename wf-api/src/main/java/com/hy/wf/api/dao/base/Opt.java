package com.hy.wf.api.dao.base;

public enum Opt {
    EQ("="), LT("<"), LTE("<="), GT(">"), GTE(">="), IN("in"), NOT_IN("not in"), LIKE("like"), NOT_LIKE("not like");

    Opt(String symbol) {
        this.symbol = symbol;
    }


    public final String symbol;

}