package com.flamingo.comeon.spec.user.constant;

import java.util.Objects;

public enum CustomerStatusEnum {
    ONLINE((short) 1, "上线"),
    OFFLINE((short) 2, "下线"),
    TO_COOP((short) 3, "待合作"),
    // 未填写门店信息用户
    NOT_EXIST((short) -1, "用户不存在");

    private short value;
    private String desc;

    CustomerStatusEnum(short value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String desc() {
        return this.desc;
    }

    public Short value() {
        return this.value;
    }

    public static CustomerStatusEnum valueToEnum(Short value) {
        for (CustomerStatusEnum statusEnum : CustomerStatusEnum.values()) {
            if (Objects.equals(statusEnum.value, value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
