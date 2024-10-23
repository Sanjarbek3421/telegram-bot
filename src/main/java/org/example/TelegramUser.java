package org.example;

public class TelegramUser {
    private String chatId;
    private String step;
    private String msg;
    private String fullName;
    private  String selectedLang;

    public String getSelectedLang() {
        return selectedLang;
    }

    public void setSelectedLang(String selectedLang) {
        this.selectedLang = selectedLang;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public TelegramUser() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "chatId='" + chatId + '\'' +
                ", step='" + step + '\'' +
                ", msg='" + msg + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
