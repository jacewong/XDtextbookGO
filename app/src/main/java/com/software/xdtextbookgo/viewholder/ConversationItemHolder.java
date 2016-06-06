package com.software.xdtextbookgo.viewholder;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.software.xdtextbookgo.R;
import com.software.xdtextbookgo.event.ConversationItemClickEvent;
import com.software.xdtextbookgo.model.Room;
import com.software.xdtextbookgo.service.ConversationHelper;
import com.software.xdtextbookgo.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by huang on 2016/6/5.
 */
public class ConversationItemHolder extends AVCommonViewHolder {

    TextView unreadView;
    TextView messageView;
    TextView timeView;
    TextView nameView;
    LinearLayout contentLayout;
    private String anotherId;

    public ConversationItemHolder(ViewGroup root) {
        super(root.getContext(), root, R.layout.conversation_item);
        initView();
    }

    public void initView() {
        unreadView = (TextView) itemView.findViewById((R.id.conversation_item_tv_unread));
        nameView = (TextView)itemView.findViewById(R.id.conversation_item_tv_name);
        timeView = (TextView)itemView.findViewById(R.id.conversation_item_tv_time);
        messageView = (TextView)itemView.findViewById(R.id.conversation_item_tv_message);
        contentLayout = (LinearLayout)itemView.findViewById(R.id.conversation_item_layout_content);
    }

    @Override
    public void bindData(Object o) {
        final Room room = (Room) o;
        AVIMConversation conversation = room.getConversation();
        if (null != conversation) {
            nameView.setText(ConversationHelper.nameOfConversation(conversation));
            anotherId = nameView.getText().toString();
            int num = room.getUnreadCount();
            Log.e("num", num+"");
            unreadView.setText(num + "");
            unreadView.setVisibility(num > 0 ? View.VISIBLE : View.GONE);

            conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                @Override
                public void done(AVIMMessage avimMessage, AVIMException e) {
                    if (null != avimMessage) {
                        Date date = new Date(avimMessage.getTimestamp());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        timeView.setText(format.format(date));
                        messageView.setText(Utils.getMessageeShorthand(getContext(), avimMessage));
                    } else {
                        timeView.setText("");
                        messageView.setText("");
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ConversationItemClickEvent(anotherId));
                }
            });
        }
    }
}
