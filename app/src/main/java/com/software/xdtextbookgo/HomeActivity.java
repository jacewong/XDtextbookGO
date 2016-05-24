package com.software.xdtextbookgo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;



import com.software.xdtextbookgo.adapter.BookInfoAdapter;
import com.software.xdtextbookgo.filter_opensource.FilterData;
import com.software.xdtextbookgo.filter_opensource.FilterView;
import com.software.xdtextbookgo.filter_opensource.ModelUtil;
import com.software.xdtextbookgo.structure.BookInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by huang on 2016/4/25.
 */
public class HomeActivity extends XDtextbookGOActivity{
    private Button btn_back;
    private ImageView back;
    private Spinner spinner_dept, spinner_grade;
    private TextView title_text;
    private List<BookInfo> infoList = new ArrayList<BookInfo>();

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


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        inittoolbar();


        //@Bind(R.id.fv_top_filter)
        fvTopFilter = (FilterView) findViewById(R.id.fv_top_filter);
        initFilterData();
        initFilterView();
        initFilterListener();


        initBookInfo();
        BookInfoAdapter adapter = new BookInfoAdapter(HomeActivity.this,R.layout.homelist_item,infoList);
        ListView listView = (ListView) findViewById(R.id.honme_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookInfo bookInfo = infoList.get(position);
                Intent intent = new Intent(HomeActivity.this, SaleDetailActivity.class);
                startActivity(intent);
            }
        });
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
        BookInfo lisan = new BookInfo(R.drawable.test, "离散数学（第三版）", "方世昌","西安电子科技大学出版社","计算机学院","大二","￥20.00","全新");
        BookInfo lisan1 = new BookInfo(R.drawable.test, "离散数学（第三版）", "方世昌","西安电子科技大学出版社", "计算机学院","大二","￥15.00","9成新");
        infoList.add(lisan);
        infoList.add(lisan1);
    }
}
