package com.zl.vo_.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.VIPProductData;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class vipAdapter extends BaseAdapter {
    public Context context;
    private List<VIPProductData.VIPProductInfo.VIPProductCell> biglist;
    private int selectedPosition = -1;// 选中的位置

    public vipAdapter(Context context, List<VIPProductData.VIPProductInfo.VIPProductCell> biglist) {
        this.context = context;
        this.biglist=biglist;

    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public int getCount() {
        return biglist.size();
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
        VIPProductData.VIPProductInfo.VIPProductCell cell=biglist.get(i);
        view= LayoutInflater.from(context).inflate(R.layout.lay_vipitem,null);
        TextView time=view.findViewById(R.id.votime);
        TextView Original_price=view.findViewById(R.id.OriginalPrice);
        TextView zhekou=view.findViewById(R.id.vo_zhekou_tv);
        TextView discount_price=view.findViewById(R.id.vofree);

        time.setText(cell.getLimit());
        Original_price.setText(cell.getPrice());
        discount_price.setText(cell.getDiscount_price()+"元");
        Original_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线（删除线）



        if(!TextUtils.isEmpty(cell.getDiscount())){
            zhekou.setText(cell.getDiscount());

        }else {
            zhekou.setVisibility(View.GONE);
        }
        RadioButton ra=view.findViewById(R.id.vorabtn);
        if(selectedPosition==i){
            ra.setChecked(true);
            ra.setBackgroundResource(R.mipmap.ra_true);
        }else {
            ra.setChecked(false);
            ra.setBackgroundResource(R.mipmap.ra_false);
        }


//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //费用
//                CreateVoVIPAccountActivityVo.total_fee="1";
//                //订单号
//                CreateVoVIPAccountActivityVo.out_trade_no="3333";
//             WxPayActivity payActivity=new WxPayActivity(context);
//
//            }
//        });

        return view;
    }
}
