package com.zl.vo_.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/12/11.
 */

public class NewGridView extends GridView {
    public NewGridView(Context context) {
        super(context);
    }

    public NewGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewGridView(Context context, AttributeSet attrs, int defStyle) {
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
