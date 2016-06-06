package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.software.xdtextbookgo.adapter.ConversationListAdapter;
import com.software.xdtextbookgo.event.ConversationItemClickEvent;
import com.software.xdtextbookgo.event.ImTypeMessageEvent;
import com.software.xdtextbookgo.model.LeanchatUser;
import com.software.xdtextbookgo.model.Room;
import com.software.xdtextbookgo.service.ConversationHelper;
import com.software.xdtextbookgo.service.ConversationManager;
import com.software.xdtextbookgo.utils.Constants;
import com.software.xdtextbookgo.utils.UserCacheUtils;
import com.software.xdtextbookgo.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by huang zhen xi on 2016/4/28.
 */
public class MyMsgActivity extends XDtextbookGOActivity {
    private TextView title_text;
    private Button btn_back;
    private ImageView back;


    protected SwipeRefreshLayout refreshLayout;

    protected RecyclerView recyclerView;

    protected ConversationListAdapter<Room> itemAdapter;
    protected LinearLayoutManager layoutManager;

    private boolean hidden;
    private ConversationManager conversationManager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymsg_layout);
        conversationManager = ConversationManager.getInstance();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_conversation_srl_pullrefresh);
        refreshLayout.setEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_conversation_srl_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        itemAdapter = new ConversationListAdapter<Room>();
        recyclerView.setAdapter(itemAdapter);
        EventBus.getDefault().register(this);
        inittoolbar();
    }
    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("消息");
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

    public void onEvent(ConversationItemClickEvent event) {
        Intent intent = new Intent(MyMsgActivity.this, MessageActivity.class);
        intent.putExtra(Constants.MEMBER_ID, event.anotherId);
        startActivity(intent);
    }
    public void onEvent(ImTypeMessageEvent event) {
        updateConversationList();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            updateConversationList();
        }
    }


    private void updateConversationList() {
        conversationManager.findAndCacheRooms(new Room.MultiRoomsCallback() {
            @Override
            public void done(List<Room> roomList, AVException exception) {
                if (filterException(exception)) {

                    updateLastMessage(roomList);
                    cacheRelatedUsers(roomList);

                    List<Room> sortedRooms = sortRooms(roomList);
                    itemAdapter.setDataList(sortedRooms);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void updateLastMessage(final List<Room> roomList) {
        for (final Room room : roomList) {
            AVIMConversation conversation = room.getConversation();
            if (null != conversation) {
                conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                    @Override
                    public void done(AVIMMessage avimMessage, AVIMException e) {
                        if (filterException(e) && null != avimMessage) {
                            room.setLastMessage(avimMessage);
                            int index = roomList.indexOf(room);
                            itemAdapter.notifyItemChanged(index);
                        }
                    }
                });
            }
        }
    }

    private void cacheRelatedUsers(List<Room> rooms) {
        List<String> needCacheUsers = new ArrayList<String>();
        for(Room room : rooms) {
            AVIMConversation conversation = room.getConversation();
            needCacheUsers.add(ConversationHelper.otherIdOfConversation(conversation));
        }
        UserCacheUtils.fetchUsers(needCacheUsers, new UserCacheUtils.CacheUserCallback() {
            @Override
            public void done(List<LeanchatUser> userList, Exception e) {
                itemAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<Room> sortRooms(final List<Room> roomList) {
        List<Room> sortedList = new ArrayList<Room>();
        if (null != roomList) {
            sortedList.addAll(roomList);
            Collections.sort(sortedList, new Comparator<Room>() {
                @Override
                public int compare(Room lhs, Room rhs) {
                    long value = lhs.getLastModifyTime() - rhs.getLastModifyTime();
                    if (value > 0) {
                        return -1;
                    } else if (value < 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return sortedList;
    }

}
