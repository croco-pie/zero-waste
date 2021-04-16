package com.zerowaste.zwb.utils;

import com.zerowaste.zwb.dto.MaterialCodesDTO;
import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.entities.MaterialCodesEntity;
import com.zerowaste.zwb.entities.WasteEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntityConverter {

    public static WasteDTO convertEntityToDTO(WasteEntity wasteEntity) {
        return WasteDTO.builder()
                .id(wasteEntity.getId())
                .code(wasteEntity.getMaterialCodesEntity().getCode())
                .name(wasteEntity.getCodeName())
                .build();
    }

    public static MaterialCodesDTO convertEntityToDTO(MaterialCodesEntity materialCodesEntity) {
        return MaterialCodesDTO.builder()
                .id(materialCodesEntity.getId())
                .code(materialCodesEntity.getCode())
                .build();
    }

}
