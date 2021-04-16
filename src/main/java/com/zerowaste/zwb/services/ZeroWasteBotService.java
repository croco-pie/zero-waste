package com.zerowaste.zwb.services;

import com.zerowaste.zwb.dto.WasteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableAutoConfiguration
public class ZeroWasteBotService {

    private final WasteService wasteService;

    public void printWaste() {
        List<WasteDTO> wasteList = wasteService.findWasteByCode("81");
        System.out.println("\n");
        System.out.println("Маркировка:");
        System.out.println(wasteList.get(0).getCode());
    }
}
