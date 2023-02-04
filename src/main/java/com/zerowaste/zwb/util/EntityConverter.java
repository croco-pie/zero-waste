package com.zerowaste.zwb.util;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.entity.WasteEntity;

public class EntityConverter {

    public static WasteDTO convertEntityToDTO(WasteEntity wasteEntity) {
        return WasteDTO.builder()
                .id(wasteEntity.getId())
                .codeNum(wasteEntity.getCodeNum())
                .codeName(wasteEntity.getCodeName())
                .codeId(wasteEntity.getCodeId())
                .codeDescription(wasteEntity.getCodeDescription())
                .build();
    }
}
