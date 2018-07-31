package com.zl.vo_.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class LifeNoteDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<String> urls;

    public LifeNoteDetailsAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.lay_lifenotedetails,null);
        ImageView imageView = view.findViewById(R.id.iv);
        if(!TextUtils.isEmpty(urls.get(i))){
            Picasso.with(context).load(urls.get(i)).placeholder(R.mipmap.qiujing).into(imageView);
        }


        return view;
    }
}
