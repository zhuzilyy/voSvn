package com.zl.vo_.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class LifeNoteMorePicAdapte extends BaseAdapter {
    private Context context;
    private List<String> picUrls;

    public LifeNoteMorePicAdapte(Context context, List<String> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
    }

    @Override
    public int getCount() {
        return picUrls.size();
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
        view= LayoutInflater.from(context).inflate(R.layout.lay_lifenote_morepic_item,null);
        ImageView imageView=view.findViewById(R.id.lifeNote_item_more_iv);
        Picasso.with(context).load(picUrls.get(i)).placeholder(R.mipmap.girl2).into(imageView);
        return view;
    }
}
