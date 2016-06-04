package com.software.xdtextbookgo;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.software.xdtextbookgo.model.BookInfo;
import com.software.xdtextbookgo.service.DoubleFormat;

/**
 * Created by huang zhen xi on 2016/4/28.
 */
public class SaleDetailActivity extends XDtextbookGOActivity {
    private TextView book_name, author_name, publisher_name, show_dept, show_grade, new_price, show_xinjiu, show_count, old_price, user_text;
    private Button btn_back, btn_sendmsg;
    private ImageView bookpic;
    private BookInfo mbook;
    DoubleFormat df = new DoubleFormat();
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saledetail_layout);
        inittoolbar();
        mbook = (BookInfo) getIntent().getSerializableExtra("book");
        btn_sendmsg = (Button) this.findViewById(R.id.bt_sendmsg);
        btn_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleDetailActivity.this, MessageActivity.class);
                intent.putExtra("user", mbook.getUser());
                startActivity(intent);
            }
        });
        initview();

    }

    private void initview(){

        user_text = (TextView) findViewById(R.id.user_text);
        user_text.setText(mbook.getUser());

        book_name = (TextView) findViewById(R.id.book_name);
        book_name.setText(mbook.getBook_name());

        author_name = (TextView) findViewById(R.id.author_name);
        author_name.setText(mbook.getAuthor_name());

        publisher_name = (TextView) findViewById(R.id.publisher_name);
        publisher_name.setText(mbook.getPublisher_name());

        show_dept = (TextView) findViewById(R.id.show_dept);
        show_dept.setText(mbook.getDept_name());

        show_grade = (TextView) findViewById(R.id.show_grade);
        show_grade.setText(mbook.getGrade_name());

        new_price = (TextView) findViewById(R.id.new_price);
        new_price.setText("￥"+ df.changeFormat(mbook.getPrice_name()));

        show_xinjiu = (TextView) findViewById(R.id.show_xinjiu);
        show_xinjiu.setText(mbook.getXinjiu_name());

        show_count = (TextView) findViewById(R.id.show_count);
        show_count.setText(mbook.getCount() + "本");

        bookpic = (ImageView) findViewById(R.id.bookpicture);
        Glide.with(bookpic.getContext())
                .load(mbook.getImageId())
                .fitCenter()
                .into(bookpic);

        old_price = (TextView) this.findViewById(R.id.old_price);
        old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//文字中间加删除线
        old_price.setText(df.changeFormat(mbook.getOri_price()));
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
