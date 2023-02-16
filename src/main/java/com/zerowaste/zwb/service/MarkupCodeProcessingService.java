package com.zerowaste.zwb.service;

import com.zerowaste.zwb.enums.WasteTypeEnum;

import java.util.List;

public interface MarkupCodeProcessingService {

    String findByWasteCodeOrMarkup(String message);
    List<String> findAllCodeNamesByType(WasteTypeEnum type);
}
