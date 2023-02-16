package com.zerowaste.zwb.enums;

public enum ButtonActionTypeEnum {
    CHOOSE_ACTION("Выберите действие:"),
    CHOOSE_MARKUP_TYPE("Список групп маркировок:");

    public final String actionTypeMessage;

    ButtonActionTypeEnum(String actionTypeMessage) {
        this.actionTypeMessage = actionTypeMessage;
    }
}
