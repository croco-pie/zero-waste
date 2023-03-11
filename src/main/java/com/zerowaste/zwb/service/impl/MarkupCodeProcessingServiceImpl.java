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
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerowaste.zwb.enums.WasteTypeEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarkupCodeProcessingServiceImpl implements MarkupCodeProcessingService {

    private final WasteRepository wasteRepository;

    public String findByWasteCodeOrMarkup(String message) {
        List<WasteDTO> found = findAllByWasteCodeOrMarkup(message);
        if (found == null || found.isEmpty()) {
            log.info("Code invalid or not found: {}", message);
            return null;
        } else {
            return processFoundResult(found);
        }
    }

    public List<String> findCodeNamesAndNumsAndDescriptionsByType(WasteTypeEnum type) {
        return wasteRepository.findAllByIsShownAndWasteTypeIs(true, type).stream()
                .sorted(Comparator.comparing(WasteEntity::getCodeNum))
                .map(wc -> String.format("%s %s", wc.getCodeNum(), wc.getCodeName()))
                .collect(Collectors.toList());
    }

    public boolean checkIfCodeNumExists(Integer codeNum) {
        List<WasteEntity> maybeFound = wasteRepository.findAllByCodeNum(codeNum);
        return !CollectionUtils.isEmpty(maybeFound);
    }

    public WasteDTO addWasteCode(Integer codeNum, String codeName, String description) {
        WasteEntity newWaste = new WasteEntity();
        newWaste.setCodeNum(codeNum);
        newWaste.setCodeName(codeName);
        newWaste.setCodeDescription(description);
        newWaste.setShown(false);
        newWaste.setWasteType(defineWasteType(codeNum));
        newWaste.setAddedByUsers(true);

        WasteEntity added = wasteRepository.save(newWaste);
        WasteDTO converted = EntityConverter.convertEntityToDTO(added);
        log.info("New waste code was successfully added to database: {}", added);

        return converted;
    }

    public List<WasteTypeEnum> findExistingWasteTypes() {
        return wasteRepository.findAllByIsShown(true).stream()
                .map(WasteEntity::getWasteType)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<WasteDTO> findAllByWasteCodeOrMarkup(String code) {
        Integer codeNum = null;
        if (code.matches(NUMERIC_REGEX)) {
            codeNum = Integer.parseInt(code);
        }

        List<WasteEntity> found = wasteRepository.findAllByCodeNumOrCodeName(codeNum, code);
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

    private WasteTypeEnum defineWasteType(Integer codeNum) {
        String wasteGroup = null;

        if (belongsToGroup(codeNum, OTHER)) {
            wasteGroup = OTHER.getWasteTypeName();
        } else if (belongsToGroup(codeNum, PLASTIC)) {
            wasteGroup = PLASTIC.getWasteTypeName();
        } else if (belongsToGroup(codeNum, PAPER)) {
            wasteGroup = PAPER.getWasteTypeName();
        } else if (belongsToGroup(codeNum, METAL)) {
            wasteGroup = METAL.getWasteTypeName();
        } else if (belongsToGroup(codeNum, TIMBER)) {
            wasteGroup = TIMBER.getWasteTypeName();
        } else if (belongsToGroup(codeNum, TEXTILE)) {
            wasteGroup = TEXTILE.getWasteTypeName();
        } else if (belongsToGroup(codeNum, GLASS)) {
            wasteGroup = GLASS.getWasteTypeName();
        } else if (belongsToGroup(codeNum, COMPOSITE)) {
            wasteGroup = COMPOSITE.getWasteTypeName();
        }

        if (Objects.isNull(wasteGroup)) {
            throw new IllegalArgumentException("Waste group is NULL, code number: " + codeNum);
        }

        return WasteTypeEnum.valueOfWasteType(wasteGroup);
    }

    private boolean belongsToGroup(Integer codeNum, WasteTypeEnum type) {
        return (codeNum >= type.wasteBeginCode) && (codeNum <= type.wasteEndCode);
    }

    private static final String NUMERIC_REGEX = "\\d{1,2}";
}
