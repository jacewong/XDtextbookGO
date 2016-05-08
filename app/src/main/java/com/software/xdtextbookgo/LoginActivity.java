package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by huang zhen xi on 2016/4/20.
 */

public class LoginActivity extends AppCompatActivity {
    private Button  btn_login, btn_register;
    private EditText et_name, et_pwd;
    private TextView title_text;
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

        btn_login =(Button) this.findViewById(R.id.bt_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,TabhostActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        btn_register =(Button) this.findViewById(R.id.bt_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
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
