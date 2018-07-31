package com.zl.vo_.main.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.vo_.R;

/**
 * Created by Administrator on 2018/1/24.
 */

public class XTitleView extends RelativeLayout {
    private TextView leftTv,rightTv;
    private ImageView ivback,ivRight;
    private RelativeLayout x_title_re;
    public XTitleView(Context context) {
        super(context);
        initViews(null);
    }

    public XTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public XTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }


    private void initViews(AttributeSet attrSet){
        LayoutInflater.from(getContext()).inflate(R.layout.lay_x_titleview,this);
        x_title_re=findViewById(R.id.x_title_re);
        leftTv=findViewById(R.id.x_title_tv_left);
        rightTv=findViewById(R.id.x_title_tv_right);
        ivback=findViewById(R.id.x_title_ivback);
        ivRight=findViewById(R.id.x_title_ivRight);

        TypedArray ta=getContext().obtainStyledAttributes(attrSet,R.styleable.XTitleView);
        int N= ta.getIndexCount();
        for (int i = 0; i <N ; i++) {
            int index=ta.getIndex(i);
            switch(index){
                case R.styleable.XTitleView_tv_left:
                    //设置左边文字
                    leftTv.setText(ta.getString(index));
                break;
                case R.styleable.XTitleView_tv_right:
                    //设置右边文字
                    rightTv.setText(ta.getString(index));
                    break;
                case R.styleable.XTitleView_isshow_back:
                    //设置back是否显示
                    setViewVisable(ivback,ta.getInt(index,0));

                    break;
                case R.styleable.XTitleView_isshow_right_iv:
                    //设置右边图标是否显示
                    setViewVisable(ivRight,ta.getInt(index,0));
                    break;
                case R.styleable.XTitleView_isshow_right_tv:
                    //设置右边文字是否显示
                    setViewVisable(rightTv,ta.getInt(index,0));
                    break;
                case R.styleable.XTitleView_back_src:
                    //设置back图标来源
                    Drawable drawable=ta.getDrawable(index);
                    BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
                    Bitmap bitmap=bitmapDrawable.getBitmap();
                    ivback.setImageBitmap(bitmap);
                    break;
                case R.styleable.XTitleView_right_iv_scr:
                    //设置右边图标的来源
                    Drawable drawable1=ta.getDrawable(index);
                    BitmapDrawable drawable2= (BitmapDrawable) drawable1;
                    Bitmap bitmap1=drawable2.getBitmap();
                    ivRight.setImageBitmap(bitmap1);
                    break;
                case R.styleable.XTitleView_title_background_color:
                    //设置title的背景颜色
                    x_title_re.setBackgroundColor(ta.getColor(index,0));
                    break;
                default:
                break;
            }
        }
    }


    /****
     * 设置title
     */
    public void xSetTitle(String titleCotent){
        leftTv.setText(titleCotent);
    }

    /***
     * back的的
     * @param l
     */
    public void setBackListener(View.OnClickListener l){
        ivback.setOnClickListener(l);
    }
    /***
     * 右边的图标和文字
     */
    public void setRightObjectListener(View.OnClickListener l){
        ivRight.setOnClickListener(l);
        rightTv.setOnClickListener(l);
    }


    /***
     * 显示隐藏某个view
     * @param v
     * @param visable
     */
    private void setViewVisable(View v, int visable) {
        switch(visable){
            case 0:
                v.setVisibility(VISIBLE);
            break;
            case 1:
                v.setVisibility(INVISIBLE);
                break;
            case 2:
                v.setVisibility(GONE);
                break;

            default:
            break;


        }
    }


}
