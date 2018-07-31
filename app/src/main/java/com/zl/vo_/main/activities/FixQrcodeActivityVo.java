package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.main_utils.myUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/22.
 */

public class FixQrcodeActivityVo extends VoBaseActivity {
    @BindView(R.id.code_image)
    public ImageView code_image;

    @BindView(R.id.ewm_head)
    public ImageView ewm_head;
    @BindView(R.id.ewm_nick)
    public TextView ewm_nick;
    @BindView(R.id.ewm_sex)
    public ImageView ewm_sex;
    @BindView(R.id.ewm_address)
    public TextView ewm_address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_fix_qrcode);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        String s=myUtils.readUser(FixQrcodeActivityVo.this).getQrcode();
        if(!TextUtils.isEmpty(s)){
            Picasso.with(FixQrcodeActivityVo.this).load(s).into(code_image);
        }else {
            Toast.makeText(this, "00112233", Toast.LENGTH_SHORT).show();
        }

        LoginData.LoginInfo.LoginAccountInfo user=myUtils.readUser(FixQrcodeActivityVo.this);
        if(user!=null){
            Glide.with(FixQrcodeActivityVo.this).load(user.getAvatar()).into(ewm_head);
            ewm_nick.setText(user.getNickname());
            ewm_address.setText(user.getAddress());
            ewm_sex.setImageResource("男".equals(user.getSex())?R.mipmap.nan:R.mipmap.nv);
        }


    }
}
