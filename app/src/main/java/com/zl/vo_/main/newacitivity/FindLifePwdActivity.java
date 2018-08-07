package com.zl.vo_.main.newacitivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.CommentBean;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.ClearEditText;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/25.
 */

public class FindLifePwdActivity extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back) public ImageView back;
    @BindView(R.id.tv_title) public TextView title;
    @BindView(R.id.loading_view) public RelativeLayout loading_view;
    @BindView(R.id.et_mibao_question_answer) public ClearEditText et_mibao_question_answer;
    @BindView(R.id.button2) Button button2;
    @BindView(R.id.et_pwdnew) MEditText et_pwdnew;
    @BindView(R.id.et_pwd_confirm) MEditText et_pwd_confirm;
    @BindView(R.id.button3) public Button button3;
    @BindView(R.id.ll_part2) public LinearLayout ll_part2;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_find_life_pwd);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText("找回密码");

    }
    @OnClick({R.id.button2,R.id.button3})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button2:
               String anwerStr= et_mibao_question_answer.getText().toString().trim();
                if(TextUtils.isEmpty(anwerStr)){
                    Toast.makeText(this, "请输入密保答案", Toast.LENGTH_SHORT).show();
                    return;
                }
                InvalidateAnswer(anwerStr);
            break;
            case R.id.button3:
                //
                String pwdStr=et_pwdnew.getText().toString().trim();
                if(TextUtils.isEmpty(pwdStr)){
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwdConfirmStr=et_pwd_confirm.getText().toString().trim();
                if(TextUtils.isEmpty(pwdConfirmStr)){
                    Toast.makeText(this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwdStr.equals(pwdConfirmStr)){
                    Toast.makeText(this, "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                setLifeNotePwd(pwdStr);


                break;

            default:
            break;


        }

    }

    private void setLifeNotePwd(String pwdStr) {
        loading_view.setVisibility(View.VISIBLE);
        LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(FindLifePwdActivity.this);
        if(user==null){
            return;
        }

        RequestParams params = new RequestParams(Url.FindLifeNotePwd_CreateNewPwdUrl2);
        params.addParameter("userid",user.getUserid());
        params.addParameter("personpass",pwdStr);
        params.addParameter("repersonpass",pwdStr);
        x.http().post(params, new MyCommonCallback<CommentBean>() {
            @Override
            public void success(CommentBean data) {
                loading_view.setVisibility(View.GONE);
                String code =data.getCode();
                if("0".equals(code)){
                    Toast.makeText(FindLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(FindLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 验证密保问题
     * @param anwerStr
     */
    private void InvalidateAnswer(String anwerStr) {
        loading_view.setVisibility(View.VISIBLE);
        LoginData.LoginInfo.LoginAccountInfo user =myUtils.readUser(FindLifePwdActivity.this);
        if(user==null){
            return;
        }
        RequestParams params =new RequestParams(Url.FindLifeNotePwd_ValidataAnswerUrl2);
        params.addParameter("userid",user.getUserid());
        params.addParameter("personkey",anwerStr);
        x.http().post(params, new MyCommonCallback<CommentBean>() {
            @Override
            public void success(CommentBean data) {
                loading_view.setVisibility(View.GONE);
               String code= data.getCode();
                if("0".equals(code)){
                    button2.setVisibility(View.GONE);
                    ll_part2.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(FindLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });

    }


}
