package com.software.xdtextbookgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.software.xdtextbookgo.R;
import com.software.xdtextbookgo.structure.BookInfo;
import com.software.xdtextbookgo.structure.Msg;

import java.util.List;

/**
 * Created by huang zhen xi on 2016/4/30.
 */
public class BookInfoAdapter extends ArrayAdapter<BookInfo> {
    private int resourceId;
    public BookInfoAdapter(Context context, int textViewResourceId, List<BookInfo> object)
    {
        super(context, textViewResourceId, object);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        BookInfo bookinfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.bookpic = (ImageView) view.findViewById(R.id.bookpic_list);
            viewHolder.bookname = (TextView) view.findViewById(R.id.bookname_list);
            viewHolder.authorname = (TextView) view.findViewById(R.id.authorname_list);
            viewHolder.publishername = (TextView) view.findViewById(R.id.publishername_list);
            viewHolder.deptname = (TextView) view.findViewById(R.id.showdept_list);
            viewHolder.gradename = (TextView) view.findViewById(R.id.showgrade_list);
            viewHolder.newprice = (TextView) view.findViewById(R.id.newprice_list);
            viewHolder.xinjiu = (TextView) view.findViewById(R.id.showxinjiu_list);
            view.setTag(viewHolder);//.***************
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

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
        viewHolder.newprice.setText(bookinfo.getPrice_name());
        viewHolder.xinjiu.setText(bookinfo.getXinjiu_name());

        return view;
    }

    class ViewHolder{
        ImageView bookpic;
        TextView bookname;
        TextView authorname;
        TextView publishername;
        TextView deptname;
        TextView gradename;
        TextView newprice;
        TextView xinjiu;
    }


}
