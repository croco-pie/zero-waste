package com.zerowaste.zwb.dto;

import com.zerowaste.zwb.enums.WasteTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class WasteDTO {

    private UUID id;
    private Integer codeNum;
    private String codeName;
    private String codeDescription;
    private WasteTypeEnum type;
}
