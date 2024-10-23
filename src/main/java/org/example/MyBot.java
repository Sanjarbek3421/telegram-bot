package org.example;

import javassist.expr.NewArray;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    List<TelegramUser> users = new ArrayList<>();



    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();
            Message message = update.getMessage();
            TelegramUser user = saveUser(chatId);

            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/list")) {
                    System.out.println(users);
                }
                if (text.equals("/start")) {
                    if (user.getFullName() != null) {
                        try {
                            setLang(chatId,user);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("ishlar qale\n" +
                                "ism familiyangizni kiriting");
                        sendMessage.getChatId();
                        sendMessage.setChatId(chatId);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        user.setStep(BotConstant.ENTER_NAME);
                    }
                } else if (user.getStep().equals(BotConstant.ENTER_NAME)) {

                    try {
                        user.setFullName(text);
                        setLang(chatId,user);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                } else if (user.getStep().equals(BotConstant.WRITE_MSG)) {
                    user.setMsg(text);
                    sendText(chatId, user.getSelectedLang().equals(BotQuery.UZ_CELECT) ?
                            "Admistrator tez orada siz bilan bog'lanadi" :
                            "qisib tursez aloqaga chiqamiz");
                }
            }
        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getFrom().getId().toString();
            String data = update.getCallbackQuery().getData();
            TelegramUser user = saveUser(chatId);
            if (user.getStep().equals(BotConstant.SELECT_LANG)) {
                if (data.equals(BotQuery.UZ_CELECT)) {
                    user.setSelectedLang(BotQuery.UZ_CELECT);
                    sendText(chatId, "Xabaringgizni qoldiring");
                } else if (data.equals(BotQuery.RU_CELECT)) {
                    user.setSelectedLang(BotQuery.RU_CELECT);
                    sendText(chatId, "xabar qoldiring ruda");
                }
                user.setStep(BotConstant.WRITE_MSG);
            }
        }
    }

    private void setLang(String chatId, TelegramUser user) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(user.getFullName() + " iltimnos  tilni tanlang");
        sendMessage.setChatId(chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> td = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButtonUZ = new InlineKeyboardButton();
        inlineKeyboardButtonUZ.setText("\uD83C\uDDFA\uD83C\uDDFF");
        inlineKeyboardButtonUZ.setCallbackData(BotQuery.UZ_CELECT);

        InlineKeyboardButton inlineKeyboardButtonRu = new InlineKeyboardButton();
        inlineKeyboardButtonRu.setText("\uD83C\uDDF7\uD83C\uDDFA");
        inlineKeyboardButtonRu.setCallbackData(BotQuery.RU_CELECT);

        td.add(inlineKeyboardButtonUZ);
        td.add(inlineKeyboardButtonRu);

        List<List<InlineKeyboardButton>> tr = new ArrayList<>();
        tr.add(td);

        inlineKeyboardMarkup.setKeyboard(tr);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
        user.setStep(BotConstant.SELECT_LANG);

    }

    private TelegramUser saveUser(String chatId) {
        for (TelegramUser user : users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        TelegramUser user = new TelegramUser();
        user.setChatId(chatId);
        users.add(user);

        return user;
    }


    @Override
    public String getBotUsername() {
        return "pdpsanjarbekbot";
    }

    @Override
    public String getBotToken() {
        return "7943126563:AAEfqirDGDR7uQWnxDShIHvH4LbFdclI360";
    }

    private void saleFileToFolder(String fileId, String fileName) throws Exception {
        GetFile getFile = new GetFile(fileId);
        File tgFile = execute(getFile);
        String fileUrl = tgFile.getFileUrl(getBotToken());
        URL url = new URL(fileUrl);
        InputStream inputStream = url.openStream();
        FileUtils.copyInputStreamToFile(inputStream, new java.io.File(fileName));

    }

    private void sendText(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
