package com.zerowaste.zwb.service;

import com.zerowaste.zwb.config.BotConfig;
import com.zerowaste.zwb.enums.InitMessageEnum;
import com.zerowaste.zwb.enums.WasteTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableAutoConfiguration
@RequiredArgsConstructor
public class ZeroWasteBotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MarkupCodeProcessingService markupCodeProcessingService;
    private final InlineKeyboardService inlineKeyboardService;

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
        Message updateMessage = update.getMessage();
        String replyMessage = null;

        if (updateMessage != null) {
            String chatId = updateMessage.getChatId().toString();
            builder.chatId(chatId);

            String messageText = updateMessage.getText();
            if (InitMessageEnum.START.message.equals(messageText)) {
                replyMessage = GREETING_MESSAGE;
            } else if (InitMessageEnum.SEARCH.message.equals(messageText)) {
                setReplyWithButtons(chatId);
            } else if (InitMessageEnum.SHOW.message.equals(messageText)) {
                setReplyWithShowWasteCodeTypeButtons(chatId);
            } else {
                replyMessage = setReplyWithWasteCodeInfo(updateMessage);
            }
        } else {
            if (update.hasCallbackQuery()) {
                setReplyForButtonAction(update.getCallbackQuery());
            } else {
                replyMessage = ERROR_MESSAGE;
            }
        }

        if (replyMessage != null) {
            replyWith(replyMessage, builder);
        }
    }

    private String setReplyWithWasteCodeInfo(Message message) {
        return markupCodeProcessingService.findByWasteCodeOrMarkup(message);
    }

    @SneakyThrows
    private void setReplyWithButtons(String chatId) {
        execute(inlineKeyboardService.buildInlineKeyboardMenu(chatId));
    }

    @SneakyThrows
    private void setReplyWithShowWasteCodeTypeButtons(String chatId) {
        List<String> wasteTypeNames = Arrays.stream(WasteTypeEnum.values())
                .map(WasteTypeEnum::getWasteTypeName)
                .collect(Collectors.toList());
        execute(inlineKeyboardService.buildInlineKeyboardWasteCodes(chatId, wasteTypeNames));
    }

    @SneakyThrows
    private void setReplyWithShowWasteCodeByTypeButtons(String chatId, WasteTypeEnum type) {
        List<String> codeNames = markupCodeProcessingService.findAllCodeNamesByType(type);
        execute(inlineKeyboardService.buildInlineKeyboardWasteCodes(chatId, codeNames));
    }

    private void setReplyForButtonAction(CallbackQuery callbackQuery) {
        inlineKeyboardService.setReplyForButtonAction(callbackQuery);
    }

    private void replyWith(String response, SendMessage.SendMessageBuilder builder) {
        try {
            builder.text(response);
            execute(builder.build());
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private static final String GREETING_MESSAGE = "Привет! \n" +
            "Это учебный бот, который помогает определить маркировку отходов. \n\n" +
            "Для открытия меню поиска нажмите /search. \n\n" +
            "Для просмотра всех маркировок нажмите /show. \n\n" +
            "Для быстрого поиска введите код или название маркировки, например, 7 или ALU.";
    private static final String ERROR_MESSAGE = "Что-то пошло не так...";
}
