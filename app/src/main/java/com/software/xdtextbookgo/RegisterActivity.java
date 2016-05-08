package com.software.xdtextbookgo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by huang zhen xi on 2016/4/28.
 */
public class RegisterActivity extends AppCompatActivity {
    private TextView title_text;
    private ImageView back;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        inittoolbar();

        final TextInputLayout nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        final TextInputLayout pwdWrapper = (TextInputLayout) findViewById(R.id.pwdWrapper);
        final TextInputLayout phoneWrapper = (TextInputLayout) findViewById(R.id.phoneWrapper);

    }
    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("注册");
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
