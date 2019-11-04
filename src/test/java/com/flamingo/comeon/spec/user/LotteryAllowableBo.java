package com.flamingo.comeon.spec.user;

public class LotteryAllowableBo {
    private Boolean allow;
    private String reason;

    public LotteryAllowableBo(Boolean allow) {
        this.allow = allow;
    }

    public LotteryAllowableBo(Boolean allow, String reason) {
        this.allow = allow;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return allow + " " + reason;
    }
}
