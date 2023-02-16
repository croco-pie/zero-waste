package com.zerowaste.zwb.enums;

public enum WasteTypeEnum {
    PLASTIC("Пластмасса", 1, 19),
    PAPER("Бумага", 20, 39),
    METAL("Металл", 40, 49),
    TIMBER("Древесина", 50, 59),
    TEXTILE("Текстиль", 60, 69),
    GLASS("Стекло", 70, 79),
    COMPOSITE("Композитный материал", 80, 99),
    OTHER("Другое", 0, 0);

    public final String wasteTypeName;
    public final int wasteBeginCode;
    public final int wasteEndCode;

    WasteTypeEnum(String wasteTypeName, int wasteBeginCode, int wasteEndCode) {
        this.wasteTypeName = wasteTypeName;
        this.wasteBeginCode = wasteBeginCode;
        this.wasteEndCode = wasteEndCode;
    }

    public String getWasteTypeName() {
        return wasteTypeName;
    }

    public int getWasteBeginCode() {
        return wasteBeginCode;
    }

    public int getWasteEndCode() {
        return wasteEndCode;
    }

    public static WasteTypeEnum valueOfWasteType(String value) {
        for (WasteTypeEnum wasteTypeEnum : values()) {
            if (wasteTypeEnum.wasteTypeName.equals(value)) {
                return wasteTypeEnum;
            }
        }
        return null;
    }
}
