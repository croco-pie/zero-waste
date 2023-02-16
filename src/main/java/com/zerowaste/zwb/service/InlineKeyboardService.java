package com.zerowaste.zwb.service;

import com.zerowaste.zwb.enums.ButtonActionTypeEnum;
import com.zerowaste.zwb.enums.MenuButtonEnum;
import com.zerowaste.zwb.enums.WasteTypeEnum;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class InlineKeyboardService {

    public SendMessage buildInlineKeyboardMenu(String chatId) {
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

    public SendMessage buildInlineKeyboardWasteCodes(String chatId, List<String> names) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRow = buildKeyBoardRows(names);

        inlineKeyboardMarkup.setKeyboard(keyboardRow);

        return buildMessage(chatId, ButtonActionTypeEnum.CHOOSE_MARKUP_TYPE.actionTypeMessage, inlineKeyboardMarkup);
    }

    public void setReplyForButtonAction(CallbackQuery callbackQuery) {
        String initMessageText = callbackQuery.getMessage().getText();
        ButtonActionTypeEnum buttonAction = Objects.requireNonNull(ButtonActionTypeEnum.valueOf(initMessageText));
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

    private void setReplyForMenuButton(String callbackData) {
        MenuButtonEnum replyDataEnum = Objects.requireNonNull(MenuButtonEnum.valueOfMessage(callbackData));

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

    private static final int MAX_ELEMENTS_IN_ROW_COUNT = 3;
}
