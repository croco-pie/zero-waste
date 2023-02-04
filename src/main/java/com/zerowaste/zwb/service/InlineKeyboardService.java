package com.zerowaste.zwb.service;

import com.zerowaste.zwb.enums.MenuButtonEnum;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class InlineKeyboardService {

    private static final String MENU_TEXT = "Выберите действие:";

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

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(MENU_TEXT);
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    private InlineKeyboardButton setButtonData(String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(text)
                .build();
    }
}
