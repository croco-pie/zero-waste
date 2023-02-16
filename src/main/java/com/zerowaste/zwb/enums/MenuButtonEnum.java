package com.zerowaste.zwb.enums;

public enum MenuButtonEnum {

    SEARCH_BY_MARKUP("Искать по маркировке"),
    SHOW_MARKUPS("Показать все маркировки");

    public final String message;

    MenuButtonEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static MenuButtonEnum valueOfMessage(String value) {
        for (MenuButtonEnum menuButtonEnum : values()) {
            if (menuButtonEnum.message.equals(value)) {
                return menuButtonEnum;
            }
        }
        return null;
    }
}
