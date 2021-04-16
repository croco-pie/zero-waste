package com.zerowaste.zwb.services;

import com.zerowaste.zwb.dto.WasteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableAutoConfiguration
public class ZeroWasteBotService {

    private final WasteService wasteService;

    public void printWaste() {
        WasteDTO waste = wasteService.findWasteByCode("81");
        log.info("Маркировка: {}", waste);
    }
}
