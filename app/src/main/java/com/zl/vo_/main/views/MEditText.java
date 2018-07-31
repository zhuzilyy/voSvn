package com.zl.vo_.main.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zl.vo_.R;


/**
 * Created by asus on 2017/2/8.
 */
public class MEditText extends RelativeLayout implements View.OnFocusChangeListener,
        View.OnClickListener, View.OnTouchListener, TextWatcher {

    public static final int  INPUTTYPE_PASSWARD=1;
    public static final int  INPUTTYPE_NORMAL=0;
    public static final int  INPUTTYPE_NUMBER=2;

    private RelativeLayout rlRoot;
    private EditText etInput;
    private ImageView imgLable;
    private ImageView imgDel;
    private ImageView imgEye;
    /**
     * 这两个值都是从xml布局文件中得来的
     */
    private boolean delEnable;
    private boolean eyeEnable;

    /**
     * @param context
     */
    private int paddingTop;
    private int paddingBottom;

    public MEditText(Context context) {
        super(context);
        init(null,context);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,context);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,context);
    }

    private void init(AttributeSet attrs,Context mContext) {
        paddingTop=getContext().getResources().getDimensionPixelSize(R.dimen.medit_padding_top);
        paddingBottom=getContext().getResources().getDimensionPixelSize(R.dimen.medit_padding_bottom);


        LayoutInflater.from(getContext()).inflate(R.layout.layout_medit, this);
        rlRoot = (RelativeLayout) findViewById(R.id.medit_root);
        imgDel = (ImageView) findViewById(R.id.medit_del);
      //  imgLable = (ImageView) findViewById(R.id.medit_lable);
        imgEye = (ImageView) findViewById(R.id.medit_eye);
        etInput = (EditText) findViewById(R.id.medit_input);

        etInput.setOnFocusChangeListener(this);
        imgDel.setOnClickListener(this);
        imgEye.setOnTouchListener(this);
        etInput.addTextChangedListener(this);
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MEditText);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.MEditText_me_text:
                    setText(a.getString(index));
                    break;
                case R.styleable.MEditText_me_hint:
                    setHint(a.getString(index));
                    break;
                case R.styleable.MEditText_me_del_enable:
                    delEnable = a.getBoolean(index, false);
                    break;
                case R.styleable.MEditText_me_eye_enable:
                    eyeEnable = a.getBoolean(index, false);
                    break;

                case R.styleable.MEditText_me_inputType:
                    setInputType(a.getInt(index,0));
                    break;
                case R.styleable.MEditText_me_hintColor:
                    //设置提示文字的颜色
                   setHintColor( a.getInt(index,0));
                    break;
                case R.styleable.MEditText_me_textColor:
                    //设置文本颜色
                  int ss=  a.getColor(index,0);

                    etInput.setTextColor(a.getColor(index,0x000000));
                    break;
                case R.styleable.MEditText_me_textSize:
                    //设置文字大小
                    etInput.setTextSize(a.getDimension(index,mContext.getResources().getDimension(R.dimen.textSize_14)));
                    break;
                case R.styleable.MEditText_me_maxLength:
                    int max=a.getInt(index,-1);
                    if(max!=-1){
                        setMaxLength(max);
                    }
                    break;
                case R.styleable.MEditText_me_background:
                    rlRoot.setBackgroundColor(a.getInt(index,0));
                    break;
                case R.styleable.MEditText_me_padding_left:
                    int mSize= (int) a.getDimension(index, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    rlRoot.setPadding(mSize,0,mSize,0);
                    break;
                case R.styleable.MEditText_me_padding_right:
                    rlRoot.setPadding(0,0,a.getInt(index,0),0);
                    break;
            }
        }
    }

    private void setHintColor(int  hintColor) {
        etInput.setHintTextColor(hintColor);
    }

    /**
     * 设置输入框最大输入的长度
     * @return
     */
    public void setMaxLength(int maxLength){
        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

    }


    public String getText(){

        return etInput.getText().toString().trim();
    }

    public void sethintColor(int color){
        etInput.setHintTextColor(color);
    }


    /**
     * Editext中的内容是以什么类型显示
     * @param type
     */
    public void setInputType(int type){
        switch (type){
            case INPUTTYPE_PASSWARD:
                etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case INPUTTYPE_NORMAL:
                etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                break;
            case INPUTTYPE_NUMBER:
                etInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
        }
    }
    public void setText(String text) {
        etInput.setText(text);
    }
    public void setHint(String text) {
        etInput.setHint(text);
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //背景的改变是由editext是否有焦点，有焦点就是选中的，没有焦点，就是没有选中
        rlRoot.setSelected(hasFocus);
        //如果删除按钮可用，并且editext有焦点，并且内容不为空，就让‘一键删除’按钮显示
        if (delEnable && hasFocus && etInput.getText().length() > 0) {
            imgDel.setVisibility(View.VISIBLE);
            //如果删除按钮可用，但是editext没有焦点，就让‘一键删除’按钮隐藏
        } else if (delEnable && !hasFocus) {
            imgDel.setVisibility(View.GONE);
        }

        /*明文密文按钮*/
        //如果眼睛是可用的，并且editext有焦点，就让眼睛显示
        if (eyeEnable && hasFocus) {
            imgEye.setVisibility(View.VISIBLE);
            //否则其他情况（比如editext没有获取焦点），就让眼睛隐藏
        } else if (eyeEnable) {
            imgEye.setVisibility(View.GONE);
        }
    }

    /**
     * 当editext的文本发生改变时 ，就会调用这个方法，在这里重新设置图片大小
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int hh=h-paddingBottom-paddingTop;
      //  imgLable.getLayoutParams().width = hh;
        imgDel.getLayoutParams().width = hh;
        imgEye.getLayoutParams().width = hh;
    }

    /**
     * 点击‘一键清除’清除Editext中的文本内容
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        etInput.getText().clear();
    }

    /**
     * 按下眼睛，明文显示，松开，密文显示
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //按下，明文显示
            etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            return true;
            //抬起，密文显示
        }


        return false;
    }

    /***
     * Editext文本改变监听
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (delEnable && etInput.getText().length() > 0) {
            imgDel.setVisibility(View.VISIBLE);
        } else if (delEnable) {
            imgDel.setVisibility(View.GONE);

        }


    }

    public void addTextChangedListener(TextWatcher watcher){
        etInput.addTextChangedListener(watcher);
    }
}
