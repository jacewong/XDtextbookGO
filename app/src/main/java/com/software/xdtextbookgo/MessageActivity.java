package com.software.xdtextbookgo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.software.xdtextbookgo.adapter.MsgAdapter;
import com.software.xdtextbookgo.model.Msg;

import java.util.ArrayList;
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
    private  String user;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        user = getIntent().getStringExtra("user");
        inittoolbar();
  /*      title_text = (TextView) this.findViewById(R.id.title_text);
        title_text.setText("User name");

        back = (ImageView) this.findViewById((R.id.back));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });  */



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
    }
    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(user);
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

    private void initMsgs(){
        Msg msg1 = new Msg("Hello guy",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("who is that?",Msg.TYPE_SENT);
        msgList.add(msg2);
    }
}
