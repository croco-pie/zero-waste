package com.zerowaste.zwb.service;

import com.zerowaste.zwb.config.BotConfig;
import com.zerowaste.zwb.dto.WasteDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@EnableAutoConfiguration
public class ZeroWasteBotService extends TelegramLongPollingBot {

    final BotConfig botConfig;

    @Autowired
    RequestProcessingService requestProcessingService;

    public ZeroWasteBotService(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String chatId;
        String responseMessage;
        WasteDTO waste;
        SendMessage.SendMessageBuilder builder = SendMessage.builder();

        Message message = update.getMessage();

        if (message != null) {
            chatId = message.getChatId().toString();
            builder.chatId(chatId);
            if (message.getText().equals("/start")) {
                builder.text("Привет! \n" +
                        "Это учебный бот, который помогает определить маркировку отходов. \n\n" +
                        "Введите код или название маркировки, например, 7 или ALU.");
            } else {
                waste = requestProcessingService.processRequest(message);
                if (waste == null) {
                    responseMessage = "Вы ввели невалидный код, либо мы пока не знаем эту маркировку :(";
                } else {
                    responseMessage = waste.getCodeDescription();
                    log.info("Found waste by code {}, id {}", waste.getCodeNum(), waste.getId());
                }
                builder.text(responseMessage);
            }
            replyWith(builder);
        } else {
            log.error("Not a text message.");
        }
    }

    private void replyWith(SendMessage.SendMessageBuilder builder) {
        try {
            execute(builder.build());
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }
}
