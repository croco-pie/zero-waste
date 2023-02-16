package com.zerowaste.zwb.enums;

public enum InitMessageEnum {

    START("/start"),
    SEARCH("/search"),
    SHOW("/show");

    public final String message;

    InitMessageEnum(String message) {
        this.message = message;
    }
}
