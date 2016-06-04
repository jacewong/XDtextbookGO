package com.software.xdtextbookgo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.software.xdtextbookgo.adapter.BookInfoAdapter;
import com.software.xdtextbookgo.service.AVService;
import com.software.xdtextbookgo.views.ActionSheetDialog;
import com.software.xdtextbookgo.views.DividerLine;
import com.software.xdtextbookgo.views.ProgressView;
import com.software.xdtextbookgo.model.BookInfo;
import com.software.xdtextbookgo.views.PullLoadMoreRecyclerView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang zhen xi on 2016/4/28.
 * 显示\修改用户发布的出售书籍
 */
public class MyReleaseActivity extends XDtextbookGOActivity {
    private BookInfoAdapter adapter;
    private ActionSheetDialog mActionSheet;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private ArrayList<BookInfo> infoList = new ArrayList<BookInfo>();
    private AVUser user = AVUser.getCurrentUser();
    private int index;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrelease_layout);
        inittoolbar();
        initRecyclerView();
    }

    private void inittoolbar() {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("出售");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        adapter = new BookInfoAdapter(infoList);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setPushRefreshEnable(false);



    /*    //分割线设置
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(3);
        dividerLine.setColor(0xFFDDDDDD);
        hao_recycleview.addItemDecoration(dividerLine);  */


        mPullLoadMoreRecyclerView.setColorSchemeResources(R.color.anotherblue, R.color.anotherblue, R.color.anotherblue,
                R.color.anotherblue);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initBookInfo();
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {


            }
        });




        initBookInfo();
        mPullLoadMoreRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickLitener(new BookInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < infoList.size()) {
                    index = position; //全局变量，存储当前点击的位置，使得内部类也能访问position
                    if (mActionSheet == null) {
                        mActionSheet = new ActionSheetDialog(MyReleaseActivity.this);
                        mActionSheet.addMenuItem("查看").addMenuItem("编辑").addMenuItem("删除");
                        mActionSheet.setMenuListener(new ActionSheetDialog.MenuListener() {
                            @Override
                            public void onItemSelected(int pos, String item) {
                                switch (pos) {
                                    case 0:
                                        //查看书籍
                                        BookInfo bookInfo = infoList.get(index);
                                        Intent intent = new Intent(MyReleaseActivity.this, SaleDetailActivity.class);
                                        intent.putExtra("book", bookInfo);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        //编辑书籍
                                        BookInfo bookInfo1 = infoList.get(index);
                                        Intent intent1 = new Intent(MyReleaseActivity.this, PublishActivity.class);
                                        intent1.putExtra("book", bookInfo1);
                                        startActivity(intent1);
                                        break;
                                    case 2:
                                        //删除书籍
                                        BookInfo bookInfo2 = infoList.get(index);
                                        String objectId = bookInfo2.getObjectId();
                                        final AVObject avObject = new AVObject("SaleInfo");
                                        avObject.setObjectId(objectId);
                                        Log.e("obj", avObject.getObjectId());
                                        avObject.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e != null) {
                                                    Log.e("del", e.getCode() + "");
                                                }
                                            }

                                        });
                                        adapter.remove(index);
                                        break;
                                    default:
                                        break;

                                }
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                    mActionSheet.show();
                }
            }
        });

    }

    private void initBookInfo() {
        infoList.clear();
        FindCallback findCallback = new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null) {
                    for (AVObject av : list) {
                        getDownloadData(list, av);
                    }
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    adapter.notifyDataSetChanged();
                } else {
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    adapter.notifyDataSetChanged();
                    showError(activity.getString(R.string.network_error));
                    Log.e("tag114", e.getCode() + "");
                }
            }
        };
        AVService.userSaleQuery(user.getUsername(), findCallback);
    }

    private void getDownloadData(List<AVObject> list, AVObject av) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setOri_price(av.getDouble("oriPrice"));
        bookInfo.setPrice_name(av.getDouble("price"));
        bookInfo.setXinjiu_name(av.getString("xinjiu"));
        bookInfo.setPublisher_name(av.getString("publisher"));
        bookInfo.setGrade_name(av.getString("grade"));
        bookInfo.setAuthor_name(av.getString("author"));
        bookInfo.setBook_name(av.getString("bookName"));
        bookInfo.setDept_name(av.getString("dept"));
        bookInfo.setCount(av.getInt("count"));
        bookInfo.setPublish_user(av.getString("userName"));
        bookInfo.setImageId(av.getAVFile("picture").getUrl());
        bookInfo.setObjectId(av.getObjectId());
        infoList.add(bookInfo);
    }

}
