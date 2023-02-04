package com.zerowaste.zwb.service;

import com.zerowaste.zwb.config.BotConfig;
import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.enums.InitMessageEnum;
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

    @Autowired
    InlineKeyboardService inlineKeyboardService;

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

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage.SendMessageBuilder builder = SendMessage.builder();
        Message message = update.getMessage();

        if (message != null) {
            String chatId = message.getChatId().toString();
            builder.chatId(chatId);

            if (InitMessageEnum.START.name().equals(message.getText())) {
                setReplyWithGreeting(builder);
            } else if (InitMessageEnum.SEARCH.name().equals(message.getText())) {
                setReplyWithButtons(chatId);
            } else {
                setReplyWithWasteCodeInfo(message, builder);
            }
            replyWith(builder);
        }

    }

    private void setReplyWithGreeting(SendMessage.SendMessageBuilder builder) {
        builder.text("Привет! \n" +
                "Это учебный бот, который помогает определить маркировку отходов. \n\n" +
                "Для открытия меню поиска нажмите /search. \n\n" +
                "Для быстрого поиска введите код или название маркировки, например, 7 или ALU.");
    }

    @SneakyThrows
    private void setReplyWithButtons(String chatId) {
        execute(inlineKeyboardService.sendInlineKeyBoardMessage(chatId));
    }

    private void setReplyWithWasteCodeInfo(Message message, SendMessage.SendMessageBuilder builder) {
        WasteDTO waste = requestProcessingService.processRequest(message);
        String responseMessage;
        if (waste == null) {
            responseMessage = "Вы ввели невалидный код, либо мы пока не знаем эту маркировку :(";
        } else {
            responseMessage = waste.getCodeDescription();
            log.info("Found waste by code {}, id {}", waste.getCodeNum(), waste.getId());
        }
        builder.text(responseMessage);
    }

    private void replyWith(SendMessage.SendMessageBuilder builder) {
        try {
            execute(builder.build());
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }
}
