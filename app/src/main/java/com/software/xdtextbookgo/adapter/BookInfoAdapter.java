package com.software.xdtextbookgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.software.xdtextbookgo.R;
import com.software.xdtextbookgo.model.BookInfo;
import com.software.xdtextbookgo.service.DoubleFormat;

import java.util.ArrayList;

/**
 * Created by huang zhen xi on 2016/4/30.
 */

public class BookInfoAdapter extends RecyclerView.Adapter< BookInfoAdapter.ViewHolder> {
    private int resourceId;
    public ArrayList<BookInfo> list = null;
    DoubleFormat df = new DoubleFormat();

    public BookInfoAdapter(ArrayList<BookInfo> datas) {
        this.list = datas;
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homelist_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        BookInfo bookinfo = list.get(position);
        Glide.with(viewHolder.bookpic.getContext())
                .load(bookinfo.getImageId())
                .fitCenter()
                .into(viewHolder.bookpic);
        //viewHolder.bookpic.setImageResource(bookinfo.getImageId());
        viewHolder.bookname.setText(bookinfo.getBook_name());
        viewHolder.authorname.setText(bookinfo.getAuthor_name());
        viewHolder.publishername.setText(bookinfo.getPublisher_name());
        viewHolder.deptname.setText(bookinfo.getDept_name());
        viewHolder.gradename.setText(bookinfo.getGrade_name());
        viewHolder.newprice.setText("￥"+ df.changeFormat(bookinfo.getPrice_name()));
        viewHolder.xinjiu.setText(bookinfo.getXinjiu_name());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                }
            });
        }
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookpic;
        public TextView bookname;
        public TextView authorname;
        public TextView publishername;
        public TextView deptname;
        public TextView gradename;
        public TextView newprice;
        public TextView xinjiu;
        public int position;
        public ViewHolder(View view) {
            super(view);
            bookpic = (ImageView) view.findViewById(R.id.bookpic_list);
            bookname = (TextView) view.findViewById(R.id.bookname_list);
            authorname = (TextView) view.findViewById(R.id.authorname_list);
            publishername = (TextView) view.findViewById(R.id.publishername_list);
            deptname = (TextView) view.findViewById(R.id.showdept_list);
            gradename = (TextView) view.findViewById(R.id.showgrade_list);
            newprice = (TextView) view.findViewById(R.id.newprice_list);
            xinjiu = (TextView) view.findViewById(R.id.showxinjiu_list);
        }
    }


    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }


}
