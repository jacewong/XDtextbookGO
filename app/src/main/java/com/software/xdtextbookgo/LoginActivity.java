package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.bumptech.glide.Glide;

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
       // title_text = (TextView) this.findViewById(R.id.title_text);
       // title_text.setText("登录界面");
        //隐藏返回按钮
       // btn_back = (Button) this.findViewById(R.id.title_back);
      //  btn_back.setVisibility(Button.INVISIBLE);
       // back = (ImageView) this.findViewById(R.id.back);
       // back.setVisibility(View.INVISIBLE);

        //********************************************
        add = (ImageView) findViewById(R.id.add);
        Glide.with(add.getContext())
                .load("http://ac-ogpnedh1.clouddn.com/qqdIKsuQ0hLD7bQq00O2BIhCBV84DpcMjMi64TYs")
                .into(add);
  /*      add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Glide.with(add.getContext())
                        .load("http://ac-ogpnedh1.clouddn.com/7TBCZAyVBkLsZDlW3vaCvwkB85u1uqiz0DtAaPsH")
                        .fitCenter()
                        .into(add);

            }
        });  */

        progressBar = createProgressBar(this,null);
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
                       // Log.e("tag",e.getCode()+"");
                        switch(e.getCode()){
                            case 210:
                                showLoginError();
                                break;
                            case 1:
                                showError(getString(R.string.error_login_more));
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

    /**
     * 在屏幕上添加一个转动条，默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param activity                    需要添加菊花的Activity
     * @param customIndeterminateDrawable 自定义的菊花图片，可以为null，此时为系统默认菊花
     * @return {ProgressBar}    菊花对象
     */
    private ProgressBar createProgressBar(Activity activity, Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }



}
