package com.zerowaste.zwb.enums;

public enum ButtonActionTypeEnum {
    CHOOSE_MARKUP_TYPE("Список групп маркировок:"),
    CHOOSE_MARKUP_CODE("Список маркировок в группе:");

    public final String actionTypeMessage;

    ButtonActionTypeEnum(String actionTypeMessage) {
        this.actionTypeMessage = actionTypeMessage;
    }

    public static ButtonActionTypeEnum valueOfButtonAction(String value) {
        for (ButtonActionTypeEnum buttonActionTypeEnum : values()) {
            if (buttonActionTypeEnum.actionTypeMessage.equals(value)) {
                return buttonActionTypeEnum;
            }
        }
        return null;
    }
}
