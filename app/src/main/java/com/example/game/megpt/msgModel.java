package com.example.game.megpt;

public class msgModel
{
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    String message;
    String sendBY;

    public msgModel(String message, String sendBY) {
        this.message = message;
        this.sendBY = sendBY;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBY() {
        return sendBY;
    }

    public void setSendBY(String sendBY) {
        this.sendBY = sendBY;
    }
}
