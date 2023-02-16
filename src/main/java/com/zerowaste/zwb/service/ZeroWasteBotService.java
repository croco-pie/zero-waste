package com.zerowaste.zwb.service;

import com.zerowaste.zwb.config.BotConfig;
import com.zerowaste.zwb.enums.ButtonActionTypeEnum;
import com.zerowaste.zwb.enums.InitMessageEnum;
import com.zerowaste.zwb.enums.MenuButtonEnum;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableAutoConfiguration
@RequiredArgsConstructor
public class ZeroWasteBotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MarkupCodeProcessingService markupCodeProcessingService;

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
                replyMessage = GENERAL_ERROR_MESSAGE;
            }
        }

        if (replyMessage != null) {
            replyWith(replyMessage, builder);
        }
    }

    private String setReplyWithWasteCodeInfo(Message message) {
        if (message.hasText()) {
            return markupCodeProcessingService.findByWasteCodeOrMarkup(message.getText().toLowerCase());
        } else {
            log.error("Not a valid code: {}", message);
            return GENERAL_ERROR_MESSAGE;
        }
    }

    @SneakyThrows
    private void setReplyWithButtons(String chatId) {
        execute(buildInlineKeyboardMenu(chatId));
    }

    private SendMessage buildInlineKeyboardMenu(String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboardRow = new ArrayList<>();

        InlineKeyboardButton searchByMarkupButton = setButtonData(MenuButtonEnum.SEARCH_BY_MARKUP.getMessage());
        InlineKeyboardButton showMarkupsButton = setButtonData(MenuButtonEnum.SHOW_MARKUPS.getMessage());

        keyboardButtons.add(searchByMarkupButton);
        keyboardButtons.add(showMarkupsButton);
        keyboardRow.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(keyboardRow);

        return buildMessage(chatId, ButtonActionTypeEnum.CHOOSE_ACTION.actionTypeMessage, inlineKeyboardMarkup);
    }

    @SneakyThrows
    private void setReplyWithShowWasteCodeTypeButtons(String chatId) {
        List<String> wasteTypeNames = Arrays.stream(WasteTypeEnum.values())
                .map(WasteTypeEnum::getWasteTypeName)
                .collect(Collectors.toList());
        execute(buildInlineKeyboardWasteCodes(chatId, wasteTypeNames));
    }

    @SneakyThrows
    private void setReplyWithShowWasteCodeByTypeButtons(String chatId, WasteTypeEnum type) {
        List<String> codeNames = markupCodeProcessingService.findAllCodeNamesByType(type);
        execute(buildInlineKeyboardWasteCodes(chatId, codeNames));
    }

    private SendMessage buildInlineKeyboardWasteCodes(String chatId, List<String> names) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRow = buildKeyBoardRows(names);

        inlineKeyboardMarkup.setKeyboard(keyboardRow);

        return buildMessage(chatId, ButtonActionTypeEnum.CHOOSE_MARKUP_TYPE.actionTypeMessage, inlineKeyboardMarkup);
    }

    private void setReplyForButtonAction(CallbackQuery callbackQuery) {
        String initMessageText = callbackQuery.getMessage().getText();
        ButtonActionTypeEnum buttonAction = Objects.requireNonNull(ButtonActionTypeEnum.valueOfButtonAction(initMessageText));
        String callbackData = callbackQuery.getData();

        switch (buttonAction) {
            case CHOOSE_ACTION:
                setReplyForMenuButton(callbackData);
            case CHOOSE_MARKUP_TYPE:
                setReplyForMarkupTypeButton(callbackData);
            default:
                // todo
        }
    }


    private void setReplyForMenuButton(String callbackData) {
        MenuButtonEnum replyDataEnum = Objects.requireNonNull(MenuButtonEnum.valueOfMenuButton(callbackData));

        // todo
        switch (replyDataEnum) {
            case SEARCH_BY_MARKUP:
            case SHOW_MARKUPS:
            default:
        }
    }

    private void setReplyForMarkupTypeButton(String callbackData) {
        WasteTypeEnum wasteTypeEnum = Objects.requireNonNull(WasteTypeEnum.valueOfWasteType(callbackData));

        // todo
        switch (wasteTypeEnum) {
            case PLASTIC:
            case PAPER:
            case METAL:
            case TIMBER:
            case TEXTILE:
            case GLASS:
            case COMPOSITE:
            case OTHER:
            default:
        }
    }

    private InlineKeyboardButton setButtonData(String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(text)
                .build();
    }

    private SendMessage buildMessage(String chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    private List<List<InlineKeyboardButton>> buildKeyBoardRows(List<String> codeNames) {
        List<List<InlineKeyboardButton>> keyboardRow = new ArrayList<>();

        addRows(keyboardRow, codeNames.size());
        addButtonsToRow(keyboardRow, codeNames);

        return keyboardRow;
    }

    private void addRows(List<List<InlineKeyboardButton>> keyboardRow, int listSize) {
        int rowCount = getRowCount(listSize);

        for (int i = 0; i < rowCount; i++) {
            keyboardRow.add(new ArrayList<>(MAX_ELEMENTS_IN_ROW_COUNT));
        }
    }

    private int getRowCount(int listSize) {
        int rowCount = listSize / MAX_ELEMENTS_IN_ROW_COUNT;
        int extraRowCount = listSize % MAX_ELEMENTS_IN_ROW_COUNT;
        if (extraRowCount != 0) {
            rowCount++;
        }

        return rowCount;
    }

    private void addButtonsToRow(List<List<InlineKeyboardButton>> keyboardRow, List<String> codeNames) {
        int rowIndex = 0;
        for (String codeName : codeNames) {
            if (keyboardRow.get(rowIndex).size() < MAX_ELEMENTS_IN_ROW_COUNT) {
                keyboardRow.get(rowIndex).add(setButtonData(codeName));
            } else {
                keyboardRow.get(++rowIndex).add(setButtonData(codeName));
            }
        }
    }

    private void replyWith(String response, SendMessage.SendMessageBuilder builder) {
        try {
            builder.text(response);
            execute(builder.build());
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private static final int MAX_ELEMENTS_IN_ROW_COUNT = 3;
    private static final String GREETING_MESSAGE = "Привет! \n" +
            "Это учебный бот, который помогает определить маркировку отходов. \n\n" +
            "Для открытия меню поиска нажмите /search. \n\n" +
            "Для просмотра всех маркировок нажмите /show. \n\n" +
            "Для быстрого поиска введите код или название маркировки, например, 7 или ALU.";
    private static final String GENERAL_ERROR_MESSAGE = "Что-то пошло не так...";
}
