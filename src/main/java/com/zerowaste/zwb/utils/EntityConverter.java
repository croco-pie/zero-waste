package com.zerowaste.zwb.utils;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.entities.WasteEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntityConverter {

    public static WasteDTO convertEntityToDTO(WasteEntity wasteEntity) {
        return WasteDTO.builder()
                .id(wasteEntity.getId())
                .code(wasteEntity.getCodeNum())
                .name(wasteEntity.getCodeName())
                .build();
    }
}
