package com.flamingo.comeon.spec.user.constant;

public enum PlatformEnum {

    APP(1, "APP"),
    H5(2, "H5"),
    WAIMAI(3, "外卖");

    private int value;
    private String desc;

    private PlatformEnum(int value, String desc) {
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
