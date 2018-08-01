package com.zl.vo_.main.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zl.vo_.R;
import com.zl.vo_.update.PermissionUtils;
import com.zl.vo_.update.UpdateAppManager;
import com.zl.vo_.utils.Url;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/4.
 */

public class AboutVoActivityVo extends VoBaseActivity implements View.OnClickListener{
    private int aaaa=100;
    @BindView(R.id.re_function)
    public RelativeLayout re_function;
    @BindView(R.id.re_Complaints)
    public RelativeLayout re_tousu;
    private UpdateAppManager manager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_about_vo);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        manager=new UpdateAppManager(this);
    }
    @OnClick({R.id.re_function,R.id.re_Complaints,R.id.re_update})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.re_function:
                Intent intent=new Intent(AboutVoActivityVo.this,Help_Feedback.class);
                intent.putExtra("url", Url.FunctionIntroduceUrl);
                intent.putExtra("param","15");
                intent.putExtra("title","功能介绍");
                startActivity(intent);
                break;
            case R.id.re_Complaints:
                Intent intent1=new Intent(AboutVoActivityVo.this,PlatformComplainsActivityVo.class);
                intent1.putExtra("title","投诉");
                startActivity(intent1);
                break;
            case R.id.re_update:
                //检查或获取权限
                boolean bb= PermissionUtils.checkOrRequestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},200,this);
                if(bb){
                    //权限已经被赋予
                    manager.getUpdateMsg();//检查更新
                }
                break;
            default:
                break;
        }
    }
}
