package com.zerowaste.zwb.services;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.repositories.WasteRepository;
import com.zerowaste.zwb.utils.EntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class WasteService {

    //    private final MaterialCodesRepository materialCodesRepository;
    private final WasteRepository wasteRepository;

    public List<WasteDTO> findWasteByCode(String code) {
        try {
            return wasteRepository.findWasteByCode(code)
                    .stream()
                    .map(EntityConverter::convertEntityToDTO)
                    .collect(toList());
        } catch (Exception e) {
            log.error("Something went wrong: {}", e.getMessage());
            return null;
        }
    }


//    public List<MaterialCodesDTO> findWasteByCode(String code) {
//        try {
//            List<MaterialCodesDTO> wasteDTOList = materialCodesRepository.findMaterialCode(code)
//                    .stream()
//                    .map(EntityConverter::convertEntityToDTO)
//                    .collect(toList());
//            return wasteDTOList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
