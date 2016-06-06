package com.software.xdtextbookgo.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.software.xdtextbookgo.R;
import com.software.xdtextbookgo.event.ImTypeMessageEvent;
import com.software.xdtextbookgo.utils.NotificationUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by huang on 2016/6/6.
 *  AVIMTypedMessage 的 handler，socket 过来的 AVIMTypedMessage 都会通过此 handler 与应用交互
 *  需要应用主动调用 AVIMMessageManager.registerMessageHandler 来注册
 *  当然，自定义的消息也可以通过这种方式来处理
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private Context context;

    public MessageHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        if (message == null || message.getMessageId() == null) {
            Log.d("msghandler","may be SDK Bug, message or message id is null");
            return;
        }

        if (!ConversationHelper.isValidConversation(conversation)) {
            Log.d("msghandler","receive msg from invalid conversation");
        }

        if (AVImClientManager.getInstance().getClientId() == null) {
            Log.d("msghandler","selfId is null, please call setupManagerWithUserId ");
            client.close(null);
        } else {
            if (!client.getClientId().equals(AVImClientManager.getInstance().getClientId())) {
                client.close(null);
            } else {
                AVImClientManager.getInstance().getRoomsTable().insertRoom(message.getConversationId());
                if (!message.getFrom().equals(client.getClientId())) {
                    if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
                        sendNotification(message, conversation);
                    }
                    AVImClientManager.getInstance().getRoomsTable().increaseUnreadCount(message.getConversationId());
                    sendEvent(message, conversation);
                }
            }
        }
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    /**
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     * @param message
     * @param conversation
     */
    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        EventBus.getDefault().post(event);
    }

    private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation) {
        if (null != conversation && null != message) {
            Intent intent = new Intent();
            intent.setAction("com.avoscloud.chat.intent.client_notification");
            intent.putExtra("conversation_id", conversation.getConversationId());
            intent.putExtra("member_id", message.getFrom());
            intent.putExtra("notification_tag", "notification_single_chat");
        }
    }
}
