package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by huang zhen xi on 2016/4/24.
 */
public class PersonalActivity extends AppCompatActivity {
    private TextView title_text, sale_text, message_text;
    private Button btn_back, btn_logout;
    private ImageView back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_layout);
        inittoolbar();
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
                Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
