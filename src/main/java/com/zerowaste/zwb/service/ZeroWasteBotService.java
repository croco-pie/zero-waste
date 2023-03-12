package com.zerowaste.zwb.service;

import com.zerowaste.zwb.config.BotConfig;
import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.enums.ButtonActionTypeEnum;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerowaste.zwb.enums.ButtonActionTypeEnum.*;
import static com.zerowaste.zwb.enums.InitMessageEnum.*;

@Slf4j
@Service
@EnableAutoConfiguration
@RequiredArgsConstructor
public class ZeroWasteBotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MarkupCodeProcessingService markupCodeProcessingService;

    private static final int MAX_ELEMENTS_IN_ROW_COUNT = 3;
    private static final String ADD_WASTE_CODE_REGEX = "(\\d+)( - )([a-zA-Z]+)( - )(...+)";
    private static final String SEARCH_WASTE_CODE_BY_NUM_REGEX = "\\d{1,2}";
    private static final String SEARCH_WASTE_CODE_BY_NAME_REGEX = "\\D{1,10}";
    private static final String GREETING_MESSAGE = "Привет!\n" +
            "Это учебный бот, который помогает определить маркировку отходов.\n\n" +
            "Для открытия меню поиска по группам маркировок нажмите /show.\n\n" +
            "Для быстрого поиска введите код или название маркировки, например, 7 или ALU.\n\n" +
            "Для добавления нового кода нажмите /add";
    private static final String ADD_WASTE_CODE_INFO_MESSAGE = "Чтобы добавить новый код, введите информацию в следующем виде:\n" +
            "Цифровой код маркировки - Буквенный код маркировки - Краткое описание\n\n" +
            "Например:\n" +
            "`41 - ALU - Алюминий, банки и тюбики от крема`\n\n" +
            "Обратите внимание, что группа присваивается автоматически по введенному цифровому коду.\n" +
            "Можно ввести один код за раз.\n" +
            "После проверки мы добавим этот код в справочник. Спасибо!";
    private static final String ADD_CODE_SUCCESS_MESSAGE = "Вы успешно добавили новую маркировку:\n\n" +
            "Цифровой код маркировки: %s\n" +
            "Буквенный код маркировки: %s\n" +
            "Краткое описание: %s\n" +
            "Автоматически присвоенная группа: %s\n\n" +
            "После проверки маркировка будет выдаваться в меню поиска.";
    private static final String ADD_CODE_ERROR_ALREADY_EXISTS_MESSAGE = "Код с таким цифровым значением уже существует.";
    private static final String GENERAL_ERROR_MESSAGE = "Что-то пошло не так...";
    private static final String INVALID_INPUT_ERROR_MESSAGE = "Вы ввели данные в неправильном формате, либо мы пока не знаем эту маркировку :(";


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
            if (START.message.equals(messageText)) {
                replyMessage = GREETING_MESSAGE;
            } else if (ADD.message.equals(messageText)) {
                replyMessage = ADD_WASTE_CODE_INFO_MESSAGE;
            } else if (SHOW.message.equals(messageText)) {
                setReplyWithShowWasteCodeTypeButtons(chatId);
            } else {
                replyMessage = setReplyForTextMessage(updateMessage);
            }
        } else {
            if (update.hasCallbackQuery()) {
                setReplyForButtonAction(update.getCallbackQuery(), builder);
            } else {
                replyMessage = GENERAL_ERROR_MESSAGE;
            }
        }

        if (replyMessage != null) {
            replyWith(replyMessage, builder);
        }
    }

    private String setReplyForTextMessage(Message message) {
        if (message.hasText()) {
            String text = message.getText();
            if (isAddNewCodeMessage(text)) {
                String[] split = text.split(" - ");
                Integer codeNum = Integer.parseInt(split[0]);
                String codeName = split[1];
                String description = split[2];

                if (markupCodeProcessingService.checkIfCodeNumExists(codeNum)) {
                    return ADD_CODE_ERROR_ALREADY_EXISTS_MESSAGE;
                } else {
                    WasteDTO newWaste = markupCodeProcessingService.addWasteCode(codeNum, codeName, description);
                    return setReplyWithAddWasteCodeInfo(newWaste);
                }
            } else if (isSearchWasteCodeInfoMessage(text)) {
                return setReplyWithWasteCodeInfo(text);
            } else {
                return INVALID_INPUT_ERROR_MESSAGE;
            }
        } else {
            log.error("Not a valid code: {}", message);
            return GENERAL_ERROR_MESSAGE;
        }
    }

    private boolean isAddNewCodeMessage(String text) {
        return text.matches(ADD_WASTE_CODE_REGEX);
    }

    private boolean isSearchWasteCodeInfoMessage(String text) {
        return text.matches(SEARCH_WASTE_CODE_BY_NAME_REGEX) || text.matches(SEARCH_WASTE_CODE_BY_NUM_REGEX);
    }

    private String setReplyWithAddWasteCodeInfo(WasteDTO waste) {
        return String.format(ADD_CODE_SUCCESS_MESSAGE, waste.getCodeNum(), waste.getCodeName(),
                waste.getCodeDescription(), waste.getType().wasteTypeName);
    }

    private String setReplyWithWasteCodeInfo(String test) {
        String maybeFound = markupCodeProcessingService.findByWasteCodeOrMarkup(test.toLowerCase());
        return Objects.requireNonNullElse(maybeFound, INVALID_INPUT_ERROR_MESSAGE);
    }

    @SneakyThrows
    private void setReplyWithShowWasteCodeTypeButtons(String chatId) {
        List<WasteTypeEnum> existingWasteTypes = markupCodeProcessingService.findExistingWasteTypes();

        List<String> wasteTypeNames = existingWasteTypes.stream()
                .map(WasteTypeEnum::getWasteTypeName)
                .collect(Collectors.toList());
        execute(buildInlineKeyboardWasteCodes(chatId, wasteTypeNames, CHOOSE_MARKUP_TYPE.actionTypeMessage));
    }

    private SendMessage buildInlineKeyboardWasteCodes(String chatId, List<String> names, String message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRow = buildKeyBoardRows(names);

        inlineKeyboardMarkup.setKeyboard(keyboardRow);

        return buildMessage(chatId, inlineKeyboardMarkup, message);
    }

    private void setReplyForButtonAction(CallbackQuery callbackQuery, SendMessage.SendMessageBuilder builder) {
        String chatId = callbackQuery.getMessage().getChatId().toString();

        String initMessageText = callbackQuery.getMessage().getText();
        ButtonActionTypeEnum buttonAction = Objects.requireNonNull(ButtonActionTypeEnum.valueOfButtonAction(initMessageText));
        String callbackData = callbackQuery.getData();

        switch (buttonAction) {
            case CHOOSE_MARKUP_TYPE: {
                setReplyForMarkupTypeButton(chatId, callbackData);
                break;
            }
            case CHOOSE_MARKUP_CODE: {
                setReplyForMarkupCodeButton(chatId, callbackData, builder);
                break;
            }
            default:
                break;
        }
    }

    @SneakyThrows
    private void setReplyForMarkupTypeButton(String chatId, String callbackData) {
        WasteTypeEnum wasteTypeEnum = Objects.requireNonNull(WasteTypeEnum.valueOfWasteType(callbackData));

        List<String> codesByType = markupCodeProcessingService.findCodeNamesAndNumsAndDescriptionsByType(wasteTypeEnum);
        execute(buildInlineKeyboardWasteCodes(chatId, codesByType, CHOOSE_MARKUP_CODE.actionTypeMessage));
    }

    @SneakyThrows
    private void setReplyForMarkupCodeButton(String chatId, String callbackData, SendMessage.SendMessageBuilder builder) {
        String[] split = callbackData.split(" ");
        String maybeFound = markupCodeProcessingService.findByWasteCodeOrMarkup(split[0]);
        String replyMessage = Objects.requireNonNullElse(maybeFound, INVALID_INPUT_ERROR_MESSAGE);

        builder.chatId(chatId);
        replyWith(replyMessage, builder);
    }

    private InlineKeyboardButton setButtonData(String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(text)
                .build();
    }

    private SendMessage buildMessage(String chatId, InlineKeyboardMarkup inlineKeyboardMarkup, String text) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
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
            builder.parseMode("MarkDown");
            execute(builder.build());
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }
}
