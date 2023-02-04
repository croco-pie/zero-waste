package com.zerowaste.zwb.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class InlineKeyboardService {

    private InlineKeyboardButton button1;
    private InlineKeyboardButton button2;

    static final String TEXT_1 = "Text1";
    static final String TEXT_2 = "Text2";

    public SendMessage sendInlineKeyBoardMessage (String chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboardRow = new ArrayList<>();

        button1 = setButtonData(TEXT_1, "You pressed " + TEXT_1);
        button2 = setButtonData(TEXT_2, "You pressed " + TEXT_2);

        keyboardButtons.add(button1);
        keyboardButtons.add(button2);
        keyboardRow.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(keyboardRow);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Example");
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    private InlineKeyboardButton setButtonData(String text, String callBackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callBackData)
                .build();
    }
}
