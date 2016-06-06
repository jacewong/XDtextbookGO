package com.software.xdtextbookgo.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.software.xdtextbookgo.model.Room;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2016/6/5.
 */
public class AVImClientManager {

    private static AVImClientManager imClientManager;

    private AVIMClient avimClient;
    private String clientId;
    private RoomsTable roomsTable;

    public synchronized static AVImClientManager getInstance() {
        if (null == imClientManager) {
            imClientManager = new AVImClientManager();
        }
        return imClientManager;
    }

    private AVImClientManager() {
    }

    public void open(Context context,String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        roomsTable = RoomsTable.getInstanceByUserId(context, clientId);

        avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(callback);
    }

    /**
     * 请在应用一启动(Application onCreate)的时候就调用，因为 SDK 一启动，就会去连接聊天服务器
     * 这里包含了一些需要设置的 handler
     * @param context
     */
    public void init(Context context) {

        // 消息处理 handler
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(context));

        // 与网络相关的 handler
        AVIMClient.setClientEventHandler(LeanchatClientEventHandler.getInstance());

        // 和 Conversation 相关的事件的 handler
        AVIMMessageManager.setConversationEventHandler(ConversationEventHandler.getInstance());

        // 签名
        //AVIMClient.setSignatureFactory(new SignatureFactory());
    }

    public void createSingleConversation(String memberId, AVIMConversationCreatedCallback callback) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("type", 0);
        avimClient.createConversation(Arrays.asList(memberId), "", attrs, false, true, callback);
    }


    public AVIMClient getClient() {
        return avimClient;
    }

    public RoomsTable getRoomsTable() {
        return roomsTable;
    }


    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }

    /**
     * 用户注销的时候调用，close 之后消息不会推送过来，也不可以进行发消息等操作
     *
     * @param callback AVException 常见于网络错误
     */
    public void closeWithCallback(final AVIMClientCallback callback) {
        avimClient.close(new AVIMClientCallback() {

            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e != null) {
                    Log.e("logout",e.getCode()+"");
                }
                if (callback != null) {
                    callback.done(avimClient, e);
                }
            }
        });
        avimClient = null;
        clientId = null;
    }

    //ChatUser
    public List<Room> findRecentRooms() {
        return AVImClientManager.getInstance().getRoomsTable().selectRooms();
    }

    /**
     * 获取 AVIMConversationQuery，用来查询对话
     *
     * @return
     */
    public AVIMConversationQuery getConversationQuery() {
        return avimClient.getQuery();
    }
}
