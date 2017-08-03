package com.example.administrator.chatdemo.bean;

/**
 * Created by dwq on 2017/7/20/020.
 * e-mail:lomapa@163.com
 */

public enum ChatMessageType {
    UnsupportedMessageType(0),
    TextMessageType(-1),
    ImageMessageType(-2),
    AudioMessageType(-3);

    int type;

    private ChatMessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static ChatMessageType getAVIMReservedMessageType(int type) {
        switch (type) {
            case -3:
                return AudioMessageType;
            case -2:
                return ImageMessageType;
            case -1:
                return TextMessageType;
            default:
                return UnsupportedMessageType;
        }
    }
}
