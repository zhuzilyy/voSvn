package com.zl.vo_.main.main_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.activities.AboutVoActivityVo;
import com.zl.vo_.main.activities.Find_GameActivityVo;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.Help_Feedback_front;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.views.DetailsTypePopupWindow;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.widget.FXPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main_FragmentFind extends Fragment implements View.OnClickListener {
    private View vv;
    @BindView(R.id.iv_add)
    public ImageView iv_add;
    @BindView(R.id.iv_search)
    public ImageView iv_search;
    public   FXPopWindow fxPopWindow;
    //---------------------------
    @BindView(R.id.re_qrcode)
    public RelativeLayout re_qcode;
    @BindView(R.id.re_game)
    public RelativeLayout re_game;
    @BindView(R.id.re_helpfeedback)
    public RelativeLayout re_helpfeedback;
    @BindView(R.id.re_aboutVO)
    public RelativeLayout re_aboutVO;
    @BindView(R.id.tv_update_flag) public TextView tv_update_flag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vv=inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this,vv);
        mInit();
        return vv;
    }

    private void mInit() {
        if(DemoApplication.hasNerVersion == 1){
            tv_update_flag.setVisibility(View.VISIBLE);
        }else if(DemoApplication.hasNerVersion == 0){
            tv_update_flag.setVisibility(View.INVISIBLE);
        }




    }
    @OnClick({R.id.iv_add,R.id.iv_search,R.id.re_qrcode,R.id.re_game,
            R.id.re_helpfeedback,R.id.re_aboutVO})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_add:
                DetailsTypePopupWindow typePopupWindow = new DetailsTypePopupWindow(getActivity(),iv_add,"","");
                typePopupWindow.setmItemsOnClick(new DetailsTypePopupWindow.ItemsOnClick() {
                    @Override
                    public void itemsOnClick(int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(getActivity(),GroupsActivity.class));
                                break;
                            //添加新的好友
                            case 1:
                                startActivity(new Intent(getActivity(),addFriendActivity_ContactsVo.class));
                                break;
                            //扫一扫
                            case 2:
                                startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                                break;
                            //帮助及反馈
                            case 3:
                                startActivity(new Intent(getActivity(),Help_Feedback.class));
                                break;
                        }
                    }
                });


                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), addFriendActivity_SearchVo.class));
                break;
            case R.id.re_qrcode:
                //二维码
                startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                break;
            case R.id.re_game:
                startActivity(new Intent(getActivity(), Find_GameActivityVo.class));
                //游戏
                break;
            case R.id.re_helpfeedback:
                //帮助与反馈
                Intent intent=new Intent(getActivity(),Help_Feedback_front.class);
              /*  intent.putExtra("url","http://47.95.115.55:8080/voadmin/home/api/page_list");
                intent.putExtra("param","16");
                intent.putExtra("title","帮助与反馈");*/
                getActivity().startActivity(intent);
                break;
            case R.id.re_aboutVO:
                //关于vo
                startActivity(new Intent(getActivity(), AboutVoActivityVo.class));
                break;


            default:
                break;
        }
    }
}
