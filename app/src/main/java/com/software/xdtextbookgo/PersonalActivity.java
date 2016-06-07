package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.software.xdtextbookgo.service.AVImClientManager;
import com.software.xdtextbookgo.service.AVService;
import com.software.xdtextbookgo.utils.Constants;

/**
 * Created by huang zhen xi on 2016/4/24.
 */
public class PersonalActivity extends XDtextbookGOActivity {
    private TextView title_text, sale_text, message_text,user_text;
    private Button btn_back, btn_logout;
    private AVUser avUser;
    private String myUser;
    /**
     * 上一次点击 back 键的时间
     * 用于双击退出的判断
     */
    private static long lastBackTime = 0;

    /**
     * 当双击 back 键在此间隔内是直接触发 onBackPressed
     */
    private final int BACK_INTERVAL = 1000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_layout);
        inittoolbar();
        user_text = (TextView) findViewById(R.id.user_text);
        AVUser user = AVUser.getCurrentUser();
        user_text.setText(user.getUsername());


        sale_text = (TextView) this.findViewById(R.id.sale_text);
        sale_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, MyReleaseActivity.class);
                startActivity(intent);
            }
        });

        myUser = avUser.getCurrentUser().getUsername();
        message_text = (TextView) this.findViewById(R.id.message_text);
        message_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AVIMClient client = AVImClientManager.getInstance().getClient();
                try{
                    client.getQuery();
                    Intent intent = new Intent(PersonalActivity.this, MyMsgActivity.class);
                    startActivity(intent);
                }catch (NullPointerException e){
                    AVImClientManager.getInstance().open(PersonalActivity.this, myUser, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (filterException(e)) {
                                Log.e("susmsg", "success");
                                Intent intent = new Intent(PersonalActivity.this, MyMsgActivity.class);
                                startActivity(intent);
                            }
                            else {
                                showError("失败");
                                showToast(e.toString());
                            }
                        }
                    });
                }

            }
        });

        btn_logout = (Button) this.findViewById(R.id.bt_exit);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle(
                                activity.getResources().getString(
                                        R.string.dialog_message_title))
                        .setMessage(
                                activity.getResources().getString(
                                        R.string.logout_message))
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(final DialogInterface dialog,
                                                        int which) {
                                        try {
                                            String clientId = AVImClientManager.getInstance().getClientId();
                                            Log.e("client", clientId);
                                            AVImClientManager.getInstance().closeWithCallback(new AVIMClientCallback() {
                                                @Override
                                                public void done(AVIMClient avimClient, AVIMException e) {
                                                    Log.e("excep", e.getCode() + "");
                                                    dialog.dismiss();
                                                    Logout();
                                                }
                                            });
                                        } catch (IllegalStateException e) {
                                            Log.e("logout_error", "状态错误");
                                            dialog.dismiss();
                                            Logout();
                                        }
                                    }
                                }).show();
            }
        });
    }
    private void Logout(){
        AVService.logout();
        Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("我的");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackTime < BACK_INTERVAL) {
            super.onBackPressed();
        } else {
            showToast("双击 back 退出");
        }
        lastBackTime = currentTime;
    }

}
