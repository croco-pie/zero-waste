package com.zerowaste.zwb.services;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.repositories.WasteRepository;
import com.zerowaste.zwb.utils.EntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WasteService {

    private final WasteRepository wasteRepository;

    // todo переделать в List
    public WasteDTO findWasteByCode(String code) {
        try {
            return EntityConverter.convertEntityToDTO(wasteRepository.findWasteByCode(code));
        } catch (Exception e) {
            log.error("Unable to find waste by code num: {}", e.getMessage());
            return null;
        }
    }

    // todo переделать в List
    public WasteDTO findWasteByCodeName(String codeName) {
        try {
            return EntityConverter.convertEntityToDTO(wasteRepository.findWasteByCodeName(codeName));
        } catch (Exception e) {
            log.error("Unable to find waste by code name {}: {}", codeName, e.getMessage());
            return null;
        }
    }
}
