package com.software.xdtextbookgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.software.xdtextbookgo.views.ProgressView;

import butterknife.OnClick;

/**
 * Created by huang zhen xi on 2016/4/20.
 */

public class LoginActivity extends XDtextbookGOActivity {
    private Button  btn_login, btn_register;
    private EditText et_name, et_pwd;
    private TextView title_text;
    private ProgressBar progressBar;
    private ImageView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        inittoolbar();
        //********************************************测试Glide所用
      /*  add = (ImageView) findViewById(R.id.add);
        Glide.with(add.getContext())
                .load("http://ac-ogpnedh1.clouddn.com/qqdIKsuQ0hLD7bQq00O2BIhCBV84DpcMjMi64TYs")
                .into(add);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Glide.with(add.getContext())
                        .load("http://ac-ogpnedh1.clouddn.com/7TBCZAyVBkLsZDlW3vaCvwkB85u1uqiz0DtAaPsH")
                        .fitCenter()
                        .into(add);

            }
        });  */

        ProgressView progressView = new ProgressView(this);
        progressBar = progressView.createProgressBar(this, null);
        progressBar.setVisibility(View.INVISIBLE);

        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        btn_login =(Button) this.findViewById(R.id.bt_login);
        btn_login.setOnClickListener(loginListener);

        if (getUserId() != null) {
            Intent mainIntent = new Intent(activity,TabhostActivity.class);
            startActivity(mainIntent);
            activity.finish();
        }
        btn_register =(Button) this.findViewById(R.id.bt_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }


    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = et_name.getText().toString();
            if(name.isEmpty()){
                showError(getString(R.string.error_name_empty));
                return;
            }
            if(password().isEmpty()){
                showError(getString(R.string.error_pwd_empty));
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            AVUser.logInInBackground(name, password(), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (avUser != null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(LoginActivity.this, TabhostActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("tag", e.getCode() + "");
                        switch(e.getCode()){
                            case 210:
                                showLoginError();
                                break;
                            case 211:
                                showError("用户名不存在");
                                break;
                            case 1:
                                showError(getString(R.string.error_login_more));
                                break;
                            default:
                                showError(getString(R.string.network_error));
                                break;

                        }

                    }
                }
            });
        }
        private String password(){
            return et_pwd.getText().toString();
        }
    };

    private void showLoginError(){
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        activity.getResources().getString(
                                R.string.error_login_error))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("登录界面");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

}
