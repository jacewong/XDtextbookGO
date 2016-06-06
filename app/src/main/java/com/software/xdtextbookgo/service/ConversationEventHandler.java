package com.software.xdtextbookgo.service;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.software.xdtextbookgo.event.ConversationChangeEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by huang on 2016/6/6.
 * 和 Conversation 相关的事件的 handler
 * 需要应用主动调用  AVIMMessageManager.setConversationEventHandler
 */
public class ConversationEventHandler extends AVIMConversationEventHandler {

    private static ConversationEventHandler eventHandler;

    public static synchronized ConversationEventHandler getInstance() {
        if (null == eventHandler) {
            eventHandler = new ConversationEventHandler();
        }
        return eventHandler;
    }

    private ConversationEventHandler() {}

    @Override
    public void onOfflineMessagesUnread(AVIMClient client, AVIMConversation conversation, int unreadCount) {
        Log.i("off","onOfflineMessagesUnread");
        super.onOfflineMessagesUnread(client, conversation, unreadCount);
    }

    @Override
    public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {
        Log.i("left","onMemberLeft");
        refreshCacheAndNotify(conversation);
    }

    @Override
    public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {
        Log.i("joined", "onMemberJoined");
        refreshCacheAndNotify(conversation);
    }

    private void refreshCacheAndNotify(AVIMConversation conversation) {
        ConversationChangeEvent conversationChangeEvent = new ConversationChangeEvent(conversation);
        EventBus.getDefault().post(conversationChangeEvent);
    }

    @Override
    public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
        Log.i("kicked","onKicked");
        refreshCacheAndNotify(conversation);
    }

    @Override
    public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {
        Log.i("invited","onInvited");
        refreshCacheAndNotify(conversation);
    }
}
