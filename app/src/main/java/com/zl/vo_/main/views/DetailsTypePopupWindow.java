package com.zl.vo_.main.views;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.main.main_utils.DensityUtils;


/**
 * ============================================================
 * <p>
 * 版 权 ： 沈阳夜鱼科技有限公司
 * <p>
 * 作 者 ： Ywp
 * <p>
 * 版 本 ： 2.0
 * <p>
 * 创建日期 ：2017.07.31 17:21
 * <p>
 * 描 述 ：
 *      招聘详情弹出框
 *
 * 修订历史 ：
 * <p>
 * ============================================================
 **/
public class DetailsTypePopupWindow extends PopupWindow implements
        View.OnClickListener {

    private Activity mActivity;
    private PopupWindow mPop;
    private TextView reportTv;
    private TextView collectionTv;
    private String reportValue;

    public DetailsTypePopupWindow(final Activity mActivity, View mView, String collectionValue, String reportValue )
    {
        super(mActivity);
        this.mActivity = mActivity;
        this.reportValue = reportValue;
        if (mPop == null) {
            View rootView = View.inflate(mActivity, R.layout.pop_details_type, null);
            rootView.setFocusable(true); // 这个很重要
            rootView.setFocusableInTouchMode(true);
            mPop = new PopupWindow(rootView,
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
            rootView.findViewById(R.id.pop_group_ll).setOnClickListener(
                    this);
            rootView.findViewById(R.id.pop_addfriend_ll).setOnClickListener(
                    this);
            rootView.findViewById(R.id.pop_scan_ll).setOnClickListener(
                    this);
            rootView.findViewById(R.id.pop_helpback_ll).setOnClickListener(
                    this);
            reportTv = (TextView) rootView.findViewById(R.id.report_tv);
            collectionTv = (TextView) rootView.findViewById(R.id.collection_tv);

        }

        //当你发现有背景色时，需给布局文件设置背景色，这样即可覆盖系统自带的背景色。
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mPop.showAsDropDown(mView,10, DensityUtils.dp2px(mActivity, -10));
    }
    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        mPop.dismiss();
        switch (v.getId())
        {
            case R.id.pop_group_ll:
                mItemsOnClick.itemsOnClick(0);
                break;
            case R.id.pop_addfriend_ll:
                mItemsOnClick.itemsOnClick(1);
                break;
            case R.id.pop_scan_ll:
                mItemsOnClick.itemsOnClick(2);

                break;
            case R.id.pop_helpback_ll:
                mItemsOnClick.itemsOnClick(3);

                break;
        }
    }

    /**********************************************  *****************************************/
    public interface ItemsOnClick {
        void itemsOnClick(int position);
    }



    ItemsOnClick mItemsOnClick;
    public void setmItemsOnClick(ItemsOnClick mItemsOnClick) {
        this.mItemsOnClick = mItemsOnClick;
    }


}
