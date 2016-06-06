package com.software.xdtextbookgo.service;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.software.xdtextbookgo.model.Room;
import com.software.xdtextbookgo.utils.AVIMConversationCacheUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huang on 2016/6/6.
 */
public class ConversationManager {
    private static ConversationManager conversationManager;

    public ConversationManager() {
    }

    public static synchronized ConversationManager getInstance() {
        if (conversationManager == null) {
            conversationManager = new ConversationManager();
        }
        return conversationManager;
    }

    public void findAndCacheRooms(final Room.MultiRoomsCallback callback) {
        final List<Room> rooms = AVImClientManager.getInstance().findRecentRooms();
        List<String> conversationIds = new ArrayList<>();
        for (Room room : rooms) {
            conversationIds.add(room.getConversationId());
        }

        if (conversationIds.size() > 0) {
            AVIMConversationCacheUtils.cacheConversations(conversationIds, new AVIMConversationCacheUtils.CacheConversationCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        callback.done(rooms, e);
                    } else {
                        callback.done(rooms, null);
                    }
                }
            });
        } else {
            callback.done(rooms, null);
        }
    }

    public void updateName(final AVIMConversation conv, String newName, final AVIMConversationCallback callback) {
        conv.setName(newName);
        conv.updateInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e != null) {
                    if (callback != null) {
                        callback.done(e);
                    }
                } else {
                    if (callback != null) {
                        callback.done(null);
                    }
                }
            }
        });
    }

}