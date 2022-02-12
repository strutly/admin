package com.tsingtec.admin.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MenuEnum {
    CATALOGUE(0),
    MENU(1),
    BTN(2);

    private final Integer type;
    public Integer getType() {
        return this.type;
    }

    private MenuEnum(Integer type) {
        this.type = type;
    }
}
