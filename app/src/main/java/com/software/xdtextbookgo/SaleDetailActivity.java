package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
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
public class SaleDetailActivity extends AppCompatActivity {
    private TextView title_text, old_price;
    private Button btn_back, btn_sendmsg;
    private ImageView back;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saledetail_layout);
        inittoolbar();
        btn_sendmsg = (Button) this.findViewById(R.id.bt_sendmsg);
        btn_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleDetailActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });


        old_price = (TextView) this.findViewById(R.id.old_price);
        old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);  //文字中间加删除线



    }

    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("出售详情");
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
