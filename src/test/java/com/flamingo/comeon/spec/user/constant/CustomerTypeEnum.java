package com.flamingo.comeon.spec.user.constant;

import java.util.Objects;

public enum CustomerTypeEnum {
    SINGLE((short) 1, "普通"),
    CHAIN((short) 2, "连锁总店"),
    CHAIN_BRANCH((short) 3, "连锁分店");

    private short value;
    private String desc;

    CustomerTypeEnum(short value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String desc() {
        return this.desc;
    }

    public Short value() {
        return this.value;
    }

    public static CustomerTypeEnum valueToEnum(Short value) {
        for (CustomerTypeEnum customerTypeEnum : CustomerTypeEnum.values()) {
            if (Objects.equals(customerTypeEnum.value, value)) {
                return customerTypeEnum;
            }
        }
        return null;
    }
}
