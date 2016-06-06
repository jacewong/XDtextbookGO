package com.software.xdtextbookgo.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by huang on 2016/6/5.
 */
public class ImTypeMessageEvent {
    public AVIMTypedMessage message;
    public AVIMConversation conversation;
}
