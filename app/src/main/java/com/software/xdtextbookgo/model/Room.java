package com.software.xdtextbookgo.model;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.software.xdtextbookgo.utils.AVIMConversationCacheUtils;

import java.util.List;

/**
 * Created by huang on 2016/6/5.
 */
public class Room {
    private AVIMMessage lastMessage;
    private String conversationId;
    private int unreadCount;

    public AVIMMessage getLastMessage() {
        return lastMessage;
    }

    public long getLastModifyTime() {
        if (lastMessage != null) {
            return lastMessage.getTimestamp();
        }

        AVIMConversation conversation = getConversation();
        if (null != conversation && null != conversation.getUpdatedAt()) {
            return conversation.getUpdatedAt().getTime();
        }

        return 0;
    }

    public AVIMConversation getConversation() {
        return AVIMConversationCacheUtils.getCacheConversation(getConversationId());
    }

    public void setLastMessage(AVIMMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public static abstract class MultiRoomsCallback {
        public abstract void done(List<Room> roomList, AVException exception);
    }
}
