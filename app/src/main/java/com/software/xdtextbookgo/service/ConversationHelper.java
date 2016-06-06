package com.software.xdtextbookgo.service;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.List;

/**
 * Created by huang on 2016/6/5.
 */
public class ConversationHelper {

    public static boolean isValidConversation(AVIMConversation conversation) {
        if (conversation == null) {
            Log.d("invalid1","invalid reason : conversation is null");
            return false;
        }
        if (conversation.getMembers() == null || conversation.getMembers().size() == 0) {
            Log.d("invalid2", "invalid reason : conversation members null or empty");
            return false;
        }
        Object type = conversation.getAttribute("type");
        if (type == null) {
            Log.d("invalid1","invalid reason : type is null");
            return false;
        }

        int typeInt = (Integer) type;
        if (typeInt == 0) {
            if (conversation.getMembers().size() != 2 ||
                    conversation.getMembers().contains(AVImClientManager.getInstance().getClientId()) == false) {
                Log.d("invalid1","invalid reason : oneToOne conversation not correct");
                return false;
            }
        } else {
            Log.d("invalid1","invalid reason : typeInt wrong");
            return false;
        }
        return true;
    }


    /**
     * 获取单聊对话的另外一个人的 userId
     *
     * @param conversation
     * @return 如果非法对话，则为 selfId
     */
    public static String otherIdOfConversation(AVIMConversation conversation) {
        if (isValidConversation(conversation)) {
                List<String> members = conversation.getMembers();
                if (members.size() == 2) {
                    if (members.get(0).equals(AVImClientManager.getInstance().getClientId())) {
                        return members.get(1);
                    } else {
                        return members.get(0);
                    }
                }
        }
        // 尽管异常，返回可以使用的 userId
        return AVImClientManager.getInstance().getClientId();
    }

    public static String nameOfConversation(AVIMConversation conversation) {
        if (isValidConversation(conversation)) {
                String otherId = otherIdOfConversation(conversation);
             //   String userName = ThirdPartUserUtils.getInstance().getUserName(otherId);
                return otherId;
        } else {
            return "";
        }
    }

    public static String titleOfConversation(AVIMConversation conversation) {
        if (isValidConversation(conversation)) {
            return nameOfConversation(conversation);
        }
         else {
            return "";
        }
    }
}
