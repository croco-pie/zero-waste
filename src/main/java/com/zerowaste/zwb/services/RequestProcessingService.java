package com.zerowaste.zwb.services;

import com.zerowaste.zwb.dto.WasteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestProcessingService {

    private final WasteService wasteService;

    public WasteDTO processRequest(Message message) {

        WasteDTO waste;

        if (message.hasText()) {

            String code = message.getText();

            if (isInt(code)) {
                waste = wasteService.findWasteByCode(code);
                log.info("Searching waste by code {}", code);
            } else {
                waste = wasteService.findWasteByCodeName(code.toLowerCase());
                log.info("Searching waste by code name {}", code);
            }
            return waste;

        } else {

            log.error("Not a valid code.");
            return null;

        }
    }

    private boolean isInt(String code) {
        try {
            log.info("Is int");
            return Integer.class.isInstance(Integer.parseInt(code));
        } catch (Exception e) {
            log.info("Is string");
            return false;
        }
    }
}
