package com.zl.vo_.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LifeNoteListData;
import com.zl.vo_.main.activities.LifeNoteDetailsActivityVo;

import java.util.List;


/**
 * Created by Administrator on 2017/11/7.
 */

public class LifeNoteAdapter extends BaseAdapter {
    private Context mContext;
    private  List<LifeNoteListData.LifeNoteListInfo.LifeNoteListList> bigList;
    public deleteLifeNoteLister noteLister;

    public LifeNoteAdapter(Context mContext, List<LifeNoteListData.LifeNoteListInfo.LifeNoteListList> bigList) {
        this.mContext = mContext;
        this.bigList=bigList;
    }

    @Override
    public int getCount() {
        return bigList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public interface deleteLifeNoteLister{
        void  deleteLifeNote(View view,int position);
    }

    public void setDeleteLifeNoteLister(deleteLifeNoteLister l){
        noteLister=l;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LifeNoteListData.LifeNoteListInfo.LifeNoteListList itemdata=bigList.get(i);
        viewHolder holder=null;
        final int currentpostion;
        currentpostion=i;

        if(view==null){
            holder=new viewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.lay_lifenote_item,null);
            holder.time=view.findViewById(R.id.lifenote_item_time);
            holder.content=view.findViewById(R.id.lifenote_item_content);
            holder.gridView=view.findViewById(R.id.lifenote_item_gridview);
            holder.ll_morePic=view.findViewById(R.id.lifeNote_morePic);
            holder.ll_onePic=view.findViewById(R.id.lifeNote_onePic);
            holder.lone_iv=view.findViewById(R.id.lifeNote_onePic_iv);
            holder.delete_lifenote_icon=view.findViewById(R.id.delete_lifenote_icon);
            holder.lifeNote_morePic=view.findViewById(R.id.lifeNote_morePic);
            holder.lifeNote_ll_click=view.findViewById(R.id.lifeNote_ll_click);
            view.setTag(i);
            view.setTag(holder);
        }else {
            holder= (viewHolder) view.getTag();

        }
        if(itemdata.getPicarr().size()>1){
            //图片大于一张
            holder.ll_morePic.setVisibility(View.VISIBLE);
            holder.ll_onePic.setVisibility(View.GONE);

            LifeNoteMorePicAdapte adapte=new LifeNoteMorePicAdapte(mContext,bigList.get(i).getPicarr());
            holder.gridView.setAdapter(adapte);
            holder.time.setText(bigList.get(i).getAddtime());
            holder.content.setText(bigList.get(i).getContent());

            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(mContext,LifeNoteDetailsActivityVo.class);
                    intent.putExtra("id",bigList.get(currentpostion).getId());
                    mContext.startActivity(intent);
                }
            });



        }else if(itemdata.getPicarr().size()==1) {
            //图片等于一张
            holder.ll_morePic.setVisibility(View.GONE);
            holder.ll_onePic.setVisibility(View.VISIBLE);
            holder.time.setText(bigList.get(i).getAddtime());
            holder.content.setText(bigList.get(i).getContent());
            Picasso.with(mContext).load(bigList.get(i).getPicarr().get(0)).placeholder(R.mipmap.girl2).into(holder.lone_iv);

        }else {
            //没有图片
            holder.ll_morePic.setVisibility(View.GONE);
            holder.ll_onePic.setVisibility(View.GONE);
            holder.time.setText(bigList.get(i).getAddtime());
            holder.content.setText(bigList.get(i).getContent());

        }

        holder.delete_lifenote_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteLister.deleteLifeNote(v,i);

            }
        });

        holder.lifeNote_ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,LifeNoteDetailsActivityVo.class);
                intent.putExtra("id",bigList.get(i).getId());
                mContext.startActivity(intent);
            }
        });
        return view;
    }




    class viewHolder{

        private TextView time;
        private TextView content;
        private LinearLayout ll_morePic;
        private LinearLayout ll_onePic;
        private GridView gridView;
        private ImageView lone_iv;
        private ImageView delete_lifenote_icon;
        private LinearLayout lifeNote_morePic;
        private LinearLayout lifeNote_ll_click;






    }

}
