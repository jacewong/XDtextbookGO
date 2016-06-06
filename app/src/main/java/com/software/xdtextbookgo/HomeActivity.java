package com.software.xdtextbookgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.software.xdtextbookgo.adapter.BookInfoAdapter;
import com.software.xdtextbookgo.filter_opensource.FilterData;
import com.software.xdtextbookgo.filter_opensource.FilterView;
import com.software.xdtextbookgo.filter_opensource.ModelUtil;
import com.software.xdtextbookgo.service.AVService;
import com.software.xdtextbookgo.views.ProgressView;
import com.software.xdtextbookgo.model.BookInfo;
import com.software.xdtextbookgo.views.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2016/4/25.
 */
public class HomeActivity extends XDtextbookGOActivity{
    private ArrayList<BookInfo> infoList = new ArrayList<BookInfo>();

//filter属性初始化
  //  @Bind(R.id.fv_top_filter)
  //  FilterView fvTopFilter;
    private int filterPosition = -1; // 点击FilterView的位置：排序(0)、筛选(1)
    private Context mContext;
    private Activity mActivity;

    FilterView fvTopFilter;
    private FilterData filterData;
    private Toolbar toolbar;
    private boolean isStickyTop = true; // 是否吸附在顶部


    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private BookInfoAdapter adapter;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        inittoolbar();
        initRecyclerView();

        //@Bind(R.id.fv_top_filter)
        fvTopFilter = (FilterView) findViewById(R.id.fv_top_filter);
        initFilterData();
        initFilterView();
        initFilterListener();

    }

    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("主页");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

//弹窗实现搜索
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.btn_search) {

                final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this,R.style.SearchAlertDialog).create();
                alertDialog.show();
                Window windowChange = alertDialog.getWindow();
                windowChange.setContentView(R.layout.search_dialog);
                final EditText search = (EditText) windowChange.findViewById(R.id.et_search);

                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);  //两行代码解决弹框中输入法不弹出问题
                Button ok = (Button) windowChange.findViewById(R.id.bt_ok);
                ok.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        String a = search.getText().toString().trim();
                        alertDialog.dismiss();
                    }
                });
                Button cancel = (Button) windowChange.findViewById(R.id.bt_cancel);
                cancel.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        alertDialog.dismiss();
                    }
                });

            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //标题中使用原装搜索  但与tabhost一起使用出现bug
   /*    // Get the SearchView and set the searchable configuration
        MenuItem searchMenuItem = menu.findItem(R.id.btn_search);
        searchMenuItem.collapseActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // SearchView searchView = (SearchView) menu.findItem(R.id.btn_search).getActionView();
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.btn_search));
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // searchView.setQueryHint("书名关键字");
        SearchView.SearchAutoComplete textView = ( SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);  //使用源码id 更改hint字体大小
        textView.setTextSize(15);     */

        return true;
    }

    private void initRecyclerView(){
        adapter = new BookInfoAdapter(infoList);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setLinearLayout();


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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int count = adapter.getItemCount();
                        Log.e("pos", count + "");

                        FindCallback findCallback = new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (list != null) {
                                    Log.e("size", list.size() + "");
                                    if (count == list.size()) {
                                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                        return;
                                    }
                                    for (int i = count; i < list.size(); i++) {
                                        if (i == count + 6) {
                                            break;
                                        }
                                        AVObject av = list.get(i);
                                        getDownloadData(list, av);
                                    }
                                    adapter.notifyDataSetChanged();
                                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                } else {
                                    adapter.notifyDataSetChanged();
                                    showError(activity.getString(R.string.network_error));
                                    Log.e("tag114", e.getCode() + "");
                                }
                            }
                        };
                        AVService.loadQuery(findCallback);
                    }
                }).start();

            }
        });



        initBookInfo();

        mPullLoadMoreRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BookInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < infoList.size()) {
                    BookInfo bookInfo = infoList.get(position);
                    Intent intent = new Intent(HomeActivity.this, SaleDetailActivity.class);
                    intent.putExtra("book", bookInfo);
                    startActivity(intent);
                }
            }
        });
    }

    //filter数据与点击事件
    private void initFilterData() {
        mContext = this;
        mActivity = this;

        // 筛选数据
        filterData = new FilterData();
        filterData.setSorts(ModelUtil.getSortData());
        filterData.setFilters(ModelUtil.getFilterData());

    }

    private void initFilterView() {
        // 设置筛选数据
        fvTopFilter.setVisibility(View.VISIBLE);
        fvTopFilter.setFilterData(mActivity, filterData);

    }

    private void initFilterListener() {

        // (真正的)筛选视图点击
        fvTopFilter.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                if (isStickyTop) {
                    filterPosition = position;
                    fvTopFilter.showFilterLayout(position);
                }
            }
        });

  /*
        // 排序Item点击
        fvTopFilter.setOnItemSortClickListener(new FilterView.OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
              //  fillAdapter(ModelUtil.getSortTravelingData(entity));
            }
        });

        // 筛选Item点击
        fvTopFilter.setOnItemFilterClickListener(new FilterView.OnItemFilterClickListener() {
            @Override
            public void onItemFilterClick(FilterEntity entity) {
              //  fillAdapter(ModelUtil.getFilterTravelingData(entity));
            }
        });
*/


    }

    private void initBookInfo(){
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
        AVService.refreshQuery(6, findCallback);
    }

    private void getDownloadData(List<AVObject> list, AVObject av){
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
        infoList.add(bookInfo);
    }
}
