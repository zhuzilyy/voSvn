package com.zl.vo_.main.activities;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.adapter.BlackListAdapter;
import com.zl.vo_.main.Entity.BlackListEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/18.
 */

public class MyBlackListActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.blackLV)
    public ListView blackLV;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    private List<BlackListEntity.BlackInfo.blackListCell> bigList=new ArrayList<>();
    private BlackListAdapter adapter;
    @BindView(R.id.re_dialog)
    public RelativeLayout re_dialog;
    @BindView(R.id.btn_confirm)
    public Button btn_confirm;
    @BindView(R.id.btn_cancel)
    public Button btn_cancel;
    private String currentIndex;
    @BindView(R.id.nodata_re)
    public RelativeLayout nodata_re;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_black_list);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        getData();
        mInit();
    }
    private void getData() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.BlackListUrl);
        params.addParameter("userid", myUtils.readUser(MyBlackListActivityVo.this).getUserid());
        x.http().post(params, new MyCommonCallback<Result<BlackListEntity>>() {
            @Override
            public void success(Result<BlackListEntity> data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    BlackListEntity listEntity=data.data;
                    BlackListEntity.BlackInfo blackInfo=listEntity.getInfo();
                    List<BlackListEntity.BlackInfo.blackListCell> cells=blackInfo.getBlack_list();
                    bigList.clear();
                    bigList.addAll(cells);
                    adapter.notifyDataSetChanged();
                }
                if(bigList.size()<=0){
                    nodata_re.setVisibility(View.VISIBLE);
                }else {
                    nodata_re.setVisibility(View.GONE);
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
    private void mInit() {
        adapter=new BlackListAdapter(MyBlackListActivityVo.this,bigList);
        blackLV.setAdapter(adapter);
        blackLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  re_dialog.setVisibility(View.VISIBLE);
                unLockBlackList();
                currentIndex=bigList.get(i).getUserid();
                return true;
            }
        });
    }

    /***
     * 解除黑名单
     */
    private void unLockBlackList() {
        final Dialog dialog = new Dialog(MyBlackListActivityVo.this);
        View vv = LayoutInflater.from(MyBlackListActivityVo.this).inflate(R.layout.lay_unlock_blacklist, null);
        dialog.setContentView(vv);
        ImageView cancel = vv.findViewById(R.id.cancel_iv);
        TextView confirm = vv.findViewById(R.id.tv_confrim);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remove_blacklist(currentIndex);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick({R.id.btn_confirm,R.id.btn_cancel})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_confirm:
                //解除黑名单
                re_dialog.setVisibility(View.GONE);
              //  Remove_blacklist(currentIndex);
                break;
            case R.id.btn_cancel:
                re_dialog.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    /**
     * 解除黑名单
     * @param currentIndex
     */
    private void Remove_blacklist(String currentIndex) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.DeleteBlackListUrl);
        params.addParameter("userid",myUtils.readUser(MyBlackListActivityVo.this).getUserid());
        params.addParameter("friend_userid",currentIndex);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    sendBroadcast(new Intent("needRefresh"));
                   finish();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("err",ex.getMessage());
                loading_view.setVisibility(View.GONE);
            }
        });

    }
}
