package com.software.xdtextbookgo.structure;

/**
 * Created by huang zhen xion 2016/4/29.
 * 聊天逻辑的消息结构体
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;
    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
    public String getContent() {
        return content;
    }
    public int getType() {
        return type;
    }
}