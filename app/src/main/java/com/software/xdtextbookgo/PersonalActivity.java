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
import com.software.xdtextbookgo.service.AVService;

/**
 * Created by huang zhen xi on 2016/4/24.
 */
public class PersonalActivity extends XDtextbookGOActivity {
    private TextView title_text, sale_text, message_text,user_text;
    private Button btn_back, btn_logout;
    private ImageView back;
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

        message_text = (TextView) this.findViewById(R.id.message_text);
        message_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, MyMsgActivity.class);
                startActivity(intent);
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
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        Logout();
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

}
