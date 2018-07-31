package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/28.
 */

public class PlatformComplainsActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.et_complain)
    public EditText et_complain;
    @BindView(R.id.btn_complain)
    public Button btn_complain;

    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_platfomcomplains);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);

        minit();
    }

    private void minit() {


    }
    @OnClick({R.id.btn_complain})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_complain:
               String content= et_complain.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(this, "投诉内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.AddComplaintForPlat);
                params.addParameter("userid", myUtils.readUser(PlatformComplainsActivityVo.this).getUserid());

                params.addParameter("reason",content);
                x.http().post(params, new MyCommonCallback<Result>() {
                    @Override
                    public void success(Result data) {
                        loading_view.setVisibility(View.VISIBLE);
                            if("0".equals(data.code)){
                                Toast.makeText(PlatformComplainsActivityVo.this, "反馈成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                    }

                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.VISIBLE);
                    }
                });

                break;
            default:
                break;
        }
    }
}
