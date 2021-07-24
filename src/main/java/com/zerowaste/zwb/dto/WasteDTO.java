package com.zerowaste.zwb.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class WasteDTO {

    private UUID id;
    private String codeNum;
    private String codeName;
    private UUID codeId;
    private String codeDescription;

}
