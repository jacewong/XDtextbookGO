package com.software.xdtextbookgo;

import android.app.TabActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by huang zhen xi on 2016/4/25.
 */
public class TabhostActivity extends TabActivity implements TabHost.OnTabChangeListener {

    static TabhostActivity Instance;
    public TabhostActivity()
    {
        Instance = this;
    }

    static TabHost tabHost;
    private ImageButton addbtn;
    private String[] TABSTRS;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tabhost);

        //每个item之间的分割线去掉，更加符合MD风格
        TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        tabWidget.setStripEnabled(false);
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        // Exit.activityList.add(this);   //后面应该有用

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addbtn = (ImageButton) findViewById(R.id.publisher_btn);
        addbtn.setColorFilter(getResources().getColor(R.color.blueAccent));
        addbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(TabhostActivity.this, PublishActivity.class);
                startActivity(intent);
            }
        });

        initData();
        initView();
    }

    //得到下面菜单栏的名字
    private void initData() {
        TABSTRS = getResources().getStringArray(R.array.tab_items);
    }

    private void initView() {
        tabHost = getTabHost();

        for (int i = 0, count = TABSTRS.length; i < count; i++) {
                TabHost.TabSpec tabSpec = tabHost
                        .newTabSpec(EnumTabInfo.getTabInfoByIndex(i).getTag())
                        .setIndicator(getTabItemView(i))
                        .setContent(getTabItemIntent(i));
                tabHost.addTab(tabSpec);
        }
        setTabItemSlectedShow(0);
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
    }

    private View getTabItemView(int index) {
        View vItem = View.inflate(this, R.layout.tab_item, null);
        ImageView imgIcon = (ImageView) vItem.findViewById(R.id.itemtab_img_icon);
        TextView txtName = (TextView) vItem.findViewById(R.id.itemtab_txt_name);
        if(index != 1)
            imgIcon.setImageResource(EnumTabInfo.getTabInfoByIndex(index).getIcon());
        txtName.setText(TABSTRS[index]);

        return vItem;
    }

    private Intent getTabItemIntent(int index) {
        Intent intent = new Intent();
        intent.setClass(this, EnumTabInfo.getTabInfoByIndex(index).getClss());
        return intent;
    }

    private void setTabItemSlectedShow(int index) {

        for (int i = 0; i < TABSTRS.length; i++) {
            View vItem = tabHost.getTabWidget().getChildAt(i);
            ImageView imgIcon = (ImageView) vItem.findViewById(R.id.itemtab_img_icon);
            TextView txtName = (TextView) vItem.findViewById(R.id.itemtab_txt_name);
            if (i == index) {
                imgIcon.setColorFilter(getResources().getColor(R.color.blueAccent));   //只用一张图进行填充图片
        //        imgIcon.setImageResource(EnumTabInfo.getTabInfoByIndex(i)
         //               .getIconSelected());
                txtName.setTextColor(getResources().getColor(
                        R.color.blueAccent));
            } else {

                    imgIcon.setColorFilter(getResources().getColor(R.color.home_tab_txt));
                    txtName.setTextColor(getResources().getColor(
                            R.color.home_tab_txt));
            }
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        setTabItemSlectedShow(EnumTabInfo.getTabInfoByTag(tabId).getIndex());
    }


}
