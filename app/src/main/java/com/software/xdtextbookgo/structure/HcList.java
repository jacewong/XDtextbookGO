package com.software.xdtextbookgo.structure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.software.xdtextbookgo.R;

/**
 * Created by huang on 2016/5/28.
 */
public class HcList extends ListView implements AbsListView.OnScrollListener {

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private MoreCallBack moreCallBack;//底部加载回调
    private View footer;
    private boolean isLoading;


    public HcList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HcList(Context context) {
        super(context);
        initView(context);
    }

    public HcList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
        //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
        //visibleItemCount：当前能看见的列表项个数（小半个也算）
        //totalItemCount：列表项共数
        //判断是否滚到最后一行
        this.firstVisibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
        //回调顺序如下
        //第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
        //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
        //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
        //由于用户的操作，屏幕产生惯性滑动时为2
        //当滚到最后一行且停止滚动时，执行加载
        if (firstVisibleItem + visibleItemCount == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                if (moreCallBack != null){
                    moreCallBack.moreCallback();
                }
            }
        }
    }


    /**
     * 添加底部加载布局
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        footer = layoutInflater.inflate(R.layout.hclist_footer, null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    /**
     * 加载完成回调
     */
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }

    public void setMoreCallBack(MoreCallBack moreCallBack) {
        this.moreCallBack = moreCallBack;
    }

    /**
     * 回调接口
     */
    public interface MoreCallBack {
        void moreCallback();
    }
}
