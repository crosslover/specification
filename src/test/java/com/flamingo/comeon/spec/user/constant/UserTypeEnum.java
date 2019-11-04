package com.flamingo.comeon.spec.user.constant;

public enum UserTypeEnum {

    INTERNAL(1, "INTERNAL"),
    BD(2, "BD"),
    WAIMAI(3, "外卖");

    private int value;
    private String desc;

    private UserTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }
}
