package com.zerowaste.zwb.service;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.enums.WasteTypeEnum;

import java.util.List;

public interface MarkupCodeProcessingService {

    String findByWasteCodeOrMarkup(String message);

    List<String> findCodeNamesAndNumsAndDescriptionsByType(WasteTypeEnum type);

    boolean checkIfCodeNumExists(Integer codeNum);

    WasteDTO addWasteCode(Integer codeNum, String codeName, String description);

    List<WasteTypeEnum> findExistingWasteTypes();
}
