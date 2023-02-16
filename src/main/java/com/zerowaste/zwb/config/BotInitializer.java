package com.zerowaste.zwb.config;

import com.zerowaste.zwb.service.ZeroWasteBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {

    private final ZeroWasteBotService bot;
    private static final int RECONNECT_TIMEOUT = 10000;

    @EventListener(ContextRefreshedEvent.class)
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
            log.info("Telegram bot has started...");
        } catch (TelegramApiRequestException e) {
            try {
                log.error("Connection error, pause for " + RECONNECT_TIMEOUT + " milliseconds");
                Thread.sleep(RECONNECT_TIMEOUT);
            } catch (Exception ignore) {
                log.error("Telegram API error occurred: ", e);
            }
        }
    }
}
