package com.software.xdtextbookgo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.software.xdtextbookgo.fragment.ChatFragment;
import com.software.xdtextbookgo.service.AVImClientManager;
import com.software.xdtextbookgo.utils.Constants;

/**
 * Created by huang zhen xi on 2016/4/28.
 */
public class MessageActivity extends XDtextbookGOActivity {

    protected ChatFragment chatFragment;
    private  String memberId;
    protected AVIMConversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        chatFragment = (ChatFragment)getFragmentManager().findFragmentById(R.id.fragment_chat);
        initByIntent(getIntent());
        inittoolbar();
    }

    private void initByIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(Constants.MEMBER_ID)) {
                memberId = extras.getString(Constants.MEMBER_ID);
                getConversation(extras.getString(Constants.MEMBER_ID));
            } else if (extras.containsKey(Constants.CONVERSATION_ID)) {
                String conversationId = extras.getString(Constants.CONVERSATION_ID);
                updateConversation(AVIMClient.getInstance(AVImClientManager.getInstance().getClientId()).getConversation(conversationId));
            } else {}
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
     */
    private void getConversation(final String memberId) {
       AVImClientManager.getInstance().createSingleConversation(memberId, new AVIMConversationCreatedCallback() {
           @Override
           public void done(AVIMConversation avimConversation, AVIMException e) {
               if (filterException(e)) {
                   AVImClientManager.getInstance().getRoomsTable().insertRoom(avimConversation.getConversationId());
                   updateConversation(avimConversation);
               }
           }
       });
    }

    protected void updateConversation(AVIMConversation conversation) {
        if (null != conversation) {
            this.conversation = conversation;
            chatFragment.setConversation(conversation);
        }
    }

    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(memberId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
