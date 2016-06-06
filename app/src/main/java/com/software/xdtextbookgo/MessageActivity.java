package com.software.xdtextbookgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.software.xdtextbookgo.adapter.MsgAdapter;
import com.software.xdtextbookgo.fragment.ChatFragment;
import com.software.xdtextbookgo.model.Msg;
import com.software.xdtextbookgo.service.AVImClientManager;
import com.software.xdtextbookgo.service.ConversationHelper;
import com.software.xdtextbookgo.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huang zhen xi on 2016/4/28.
 */
public class MessageActivity extends XDtextbookGOActivity {
    private TextView title_text;
    private Button btn_back;
    private ImageView back,send;
    private ListView msg_list_view;
    private EditText inputText;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();

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
   /* private void getConversation(final String memberId) {
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.withMembers(Arrays.asList(memberId), true);
        conversationQuery.whereEqualTo("customConversationType", 1);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (filterException(e)) {
                    //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
                    if (null != list && list.size() > 0) {
                        chatFragment.setConversation(list.get(0));
                    } else {
                        HashMap<String, Object> attributes = new HashMap<String, Object>();
                        attributes.put("customConversationType", 1);
                        client.createConversation(Arrays.asList(memberId), null, attributes, false, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation avimConversation, AVIMException e) {
                                chatFragment.setConversation(avimConversation);
                            }
                        });
                    }
                }
            }
        });
    }  */


 /*   protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        memberId = getIntent().getStringExtra("memberId");
        inittoolbar();
        initMsgs();
        adapter = new MsgAdapter(MessageActivity.this, R.layout.msg_item, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (ImageView) findViewById(R.id.send);
        msg_list_view = (ListView) findViewById(R.id.msg_list_view);
        msg_list_view.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String content = inputText.getText().toString();
                if (!"".equals(content)){
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msg_list_view.setSelection(msgList.size());
                    inputText.setText("");
                }
            }
        });
    }  */

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
