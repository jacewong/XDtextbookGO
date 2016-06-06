package com.software.xdtextbookgo.event;

import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by huang on 2016/6/6.
 */
//name, member change event
public class ConversationChangeEvent {
    private AVIMConversation conv;

    public ConversationChangeEvent(AVIMConversation conv) {
        this.conv = conv;
    }

    public AVIMConversation getConv() {
        return conv;
    }
}
