package com.zl.vo_.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/11/11.
 */


public class NewListview extends ListView {

    public NewListview(Context context) {
        super(context);
    }

    public NewListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);//重新计算listview高度
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
