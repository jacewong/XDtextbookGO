package com.software.xdtextbookgo.service;
/**
 * Created by huang on 2016/5/4.
 */
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.software.xdtextbookgo.R;
import com.software.xdtextbookgo.adapter.FilterOneAdapter;
import com.software.xdtextbookgo.model.FilterData;
import com.software.xdtextbookgo.model.FilterEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class FilterView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.tv_sort)
    TextView tvSort;
    @Bind(R.id.iv_sort_arrow)
    ImageView ivSortArrow;
    @Bind(R.id.tv_filter)
    TextView tvFilter;
    @Bind(R.id.iv_filter_arrow)
    ImageView ivFilterArrow;
    @Bind(R.id.ll_sort)
    LinearLayout llSort;
    @Bind(R.id.ll_filter)
    LinearLayout llFilter;
    @Bind(R.id.lv_right)
    ListView lvRight;
    @Bind(R.id.ll_content_list_view)
    LinearLayout llContentListView;
    @Bind(R.id.view_mask_bg)
    View viewMaskBg;

    private Context mContext;
    private Activity mActivity;
    private boolean isShowing = false;
    private int filterPosition = -1;
    private int panelHeight;
    private FilterData filterData;

    private FilterEntity selectedDeptEntity; // 被选择的学院项
    private FilterEntity selectedGradeEntity; // 被选择的年级项

    private FilterOneAdapter deptAdapter;
    private FilterOneAdapter gradeAdapter;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout, this);
        ButterKnife.bind(this, view);

        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        viewMaskBg.setVisibility(GONE);
        llContentListView.setVisibility(GONE);
    }

    private void initListener() {
        llSort.setOnClickListener(this);
        llFilter.setOnClickListener(this);
        viewMaskBg.setOnClickListener(this);
        llContentListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sort:
                filterPosition = 0;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_filter:
                filterPosition = 1;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.view_mask_bg:   //点击空白处隐藏
                hide();
                break;
        }

    }

    // 复位筛选的显示状态
    public void resetFilterStatus() {
        tvSort.setTextColor(mContext.getResources().getColor(R.color.font_black_2));
                ivSortArrow.setImageResource(R.drawable.home_down_arrow);

        tvFilter.setTextColor(mContext.getResources().getColor(R.color.font_black_2));
        ivFilterArrow.setImageResource(R.drawable.home_down_arrow);
    }

    // 复位所有的状态
    public void resetAllStatus() {
        resetFilterStatus();
        hide();
    }

    // 显示筛选布局
    public void showFilterLayout(int position) {
        resetFilterStatus();
        switch (position) {
            case 0:
                setDeptAdapter();
                break;
            case 1:
                setGradeAdapter();
                break;
        }

        if (isShowing) return ;
        show();
    }


    // 设置院系数据
    private void setDeptAdapter() {
        tvSort.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        ivSortArrow.setImageResource(R.drawable.home_up_arrow);
        lvRight.setVisibility(VISIBLE);
        deptAdapter = new FilterOneAdapter(mContext, filterData.getDept());
        lvRight.setAdapter(deptAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDeptEntity = filterData.getDept().get(position);
                deptAdapter.setSelectedEntity(selectedDeptEntity);
                hide();
                if (onItemDeptClickListener != null) {
                    onItemDeptClickListener.onItemDeptClick(selectedDeptEntity);
                }
            }
        });
    }

    // 设置年级数据
    private void setGradeAdapter() {
        tvFilter.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        ivFilterArrow.setImageResource(R.drawable.home_up_arrow);
        lvRight.setVisibility(VISIBLE);
        gradeAdapter = new FilterOneAdapter(mContext, filterData.getGrade());
        lvRight.setAdapter(gradeAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGradeEntity = filterData.getGrade().get(position);
                gradeAdapter.setSelectedEntity(selectedGradeEntity);
                hide();
                if (onItemGradeClickListener != null) {
                    onItemGradeClickListener.onItemGradeClick(selectedGradeEntity);
                }
            }
        });
    }

    // 动画显示
    private void show() {
        isShowing = true;
        viewMaskBg.setVisibility(VISIBLE);
        llContentListView.setVisibility(VISIBLE);
        llContentListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llContentListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                panelHeight = llContentListView.getHeight();
                ObjectAnimator.ofFloat(llContentListView, "translationY", -panelHeight, 0).setDuration(200).start();
            }
        });
    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetFilterStatus();
        viewMaskBg.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(llContentListView, "translationY", 0, -panelHeight).setDuration(200).start();
    }


    // 设置筛选数据
    public void setFilterData(Activity activity, FilterData filterData) {
        this.mActivity = activity;
        this.filterData = filterData;
    }

    // 是否显示
    public boolean isShowing() {
        return isShowing;
    }

    // 筛选视图点击
    private OnFilterClickListener onFilterClickListener;
    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }
    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

    // 学院Item点击
    private OnItemDeptClickListener onItemDeptClickListener;
    public void setOnItemDeptClickListener(OnItemDeptClickListener onItemDeptClickListener) {
        this.onItemDeptClickListener = onItemDeptClickListener;
    }
    public interface OnItemDeptClickListener {
        void onItemDeptClick(FilterEntity entity);
    }

    // 年级Item点击
    private OnItemGradeClickListener onItemGradeClickListener;
    public void setOnItemGradeClickListener(OnItemGradeClickListener onItemGradeClickListener) {
        this.onItemGradeClickListener = onItemGradeClickListener;
    }
    public interface OnItemGradeClickListener {
        void onItemGradeClick(FilterEntity entity);
    }

}
