package com.zerowaste.zwb.service.impl;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.entity.WasteEntity;
import com.zerowaste.zwb.enums.WasteTypeEnum;
import com.zerowaste.zwb.repository.WasteRepository;
import com.zerowaste.zwb.service.MarkupCodeProcessingService;
import com.zerowaste.zwb.util.EntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarkupCodeProcessingServiceImpl implements MarkupCodeProcessingService {

    private final WasteRepository wasteRepository;

    public String findByWasteCodeOrMarkup(String message) {
        List<WasteDTO> found = findAllByWasteCodeOrMarkup(message);
        if (found == null || found.isEmpty()) {
            log.info("Code invalid or not found: {}", message);
            return INVALID_INPUT_ERROR_MESSAGE;
        } else {
            return processFoundResult(found);
        }
    }

    public List<String> findAllCodeNamesByType(WasteTypeEnum type) {
        return wasteRepository.findAllByIsShownAndWasteTypeIs(true, type).stream()
                .map(WasteEntity::getCodeName)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<WasteDTO> findAllByWasteCodeOrMarkup(String code) {
        List<WasteEntity> found = wasteRepository.findAllByCodeNumOrCodeName(code, code);
        return found.stream().map(EntityConverter::convertEntityToDTO).collect(Collectors.toList());
    }

    private String processFoundResult(List<WasteDTO> found) {
        if (found.size() == 1) {
            WasteDTO waste = found.get(0);
            log.info("Found waste by code {}, id {}", waste.getCodeNum(), waste.getId());
            return waste.getCodeDescription();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            found.forEach(waste -> {
                stringBuilder.append(waste.getCodeDescription());
                stringBuilder.append("\n\n");
            });

            return stringBuilder.toString();
        }
    }

    private static final String INVALID_INPUT_ERROR_MESSAGE = "Вы ввели невалидный код, либо мы пока не знаем эту маркировку :(";
}
