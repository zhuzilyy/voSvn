package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.SearchContactsEntivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.util.Utils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/21.
 */

public class addFriendActivity_SearchVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back)
    public ImageView back;
    @BindView(R.id.et_search)
    public EditText et_search;
    @BindView(R.id.result_searchContact_re)
    public RelativeLayout result_searchContact_re;
    @BindView(R.id.searchContacts_head)
    public CircleImageView searchContacts_head;
    @BindView(R.id.searchContacts_name)
    public TextView searchContacts_name;
    @BindView(R.id.searchContacts_vo)
    public TextView searchContacts_vo;

    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @BindView(R.id.fdj_search)
    public ImageView fdj_search;
    public SearchContactsEntivity.SearchContactsInfo.SearchContactsAccountInfo account_info;

    @BindView(R.id.ll_search_out)
    public LinearLayout ll_search_out;
    @BindView(R.id.ll_search_inner)
    public LinearLayout ll_search_inner;
    @BindView(R.id.tv_search_name)
    public TextView tv_search_name;
    @BindView(R.id.ll_search_noName)
    public LinearLayout ll_search_noName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_addfriend_final);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }
    private void mInit() {

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
               String s = et_search.getText().toString();
               if(!TextUtils.isEmpty(s)){
                   ll_search_out.setVisibility(View.VISIBLE);
                   tv_search_name.setText(s);
                   ll_search_noName.setVisibility(View.GONE);
               }else {
                   ll_search_out.setVisibility(View.GONE);

               }
            }
        });

        ll_search_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.downInput(addFriendActivity_SearchVo.this,fdj_search);
                loading_view.setVisibility(View.VISIBLE);
                result_searchContact_re.setVisibility(View.GONE);
                RequestParams params=new RequestParams(Url.FindFriendURL);
                params.addParameter("keyword",et_search.getText().toString());
                params.addParameter("userid", myUtils.readUser(addFriendActivity_SearchVo.this).getUserid());
                x.http().post(params, new MyCommonCallback<Result<SearchContactsEntivity>>() {
                    @Override
                    public void success(Result<SearchContactsEntivity> data) {
                        Log.i("ss",data.info);
                        loading_view.setVisibility(View.GONE);
                        if("0".equals(data.code)){

                            SearchContactsEntivity dataEntity=data.data;
                            SearchContactsEntivity.SearchContactsInfo contactsInfo= dataEntity.getInfo();
                            if(contactsInfo!=null){
                                account_info=  contactsInfo.getAccount_info();
                                if(account_info!=null){
                                    if(!TextUtils.isEmpty(account_info.getAccount())){
                                        DemoApplication.accountInfo=account_info;
                                        //显示搜索条目
                                        //Toast.makeText(addFriendActivity_SearchVo.this, "信息="+account_info.getAccount(), Toast.LENGTH_SHORT).show();
                                  /*  result_searchContact_re.setVisibility(View.VISIBLE);
                                    Picasso.with(addFriendActivity_SearchVo.this).load(account_info.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(searchContacts_head);
                                    searchContacts_name.setText("昵称："+account_info.getNickname());
                                    searchContacts_vo.setText("voID: "+account_info.getAccount());*/

                                        Intent intent=new Intent(addFriendActivity_SearchVo.this,UserDetailsActivityVo.class);
                                        intent.putExtra("HXid",account_info.getHuanxin_account());
                                        intent.putExtra("way",account_info.getApply_way());
                                        //是否需要验证
                                        intent.putExtra("code",account_info.getFriend_apply());
                                        //账号
                                        intent.putExtra("account",account_info.getAccount());
                                        startActivity(intent);


                                    }
                                }else {
                                    result_searchContact_re.setVisibility(View.GONE);
                                    ll_search_out.setVisibility(View.GONE);
                                    ll_search_noName.setVisibility(View.VISIBLE);
                                }
                            }else {
                                result_searchContact_re.setVisibility(View.GONE);
                                ll_search_out.setVisibility(View.GONE);
                                ll_search_noName.setVisibility(View.VISIBLE);
                            }
                        }else {
                            result_searchContact_re.setVisibility(View.GONE);
                            ll_search_out.setVisibility(View.GONE);
                            ll_search_noName.setVisibility(View.VISIBLE);
                        }

                    }
                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                        Log.i("err",ex+"s搜索错误");
                    }
                });
            }



        });






        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utils.downInput(addFriendActivity_SearchVo.this,fdj_search);
                    if (null == getCurrentFocus()) {
                        return false;
                    }
                    //在这个地方进行搜索
                    String SearchContent=v.getText().toString().trim();
                    if(TextUtils.isEmpty(SearchContent)){
                        Toast.makeText(addFriendActivity_SearchVo.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    loading_view.setVisibility(View.VISIBLE);
                    result_searchContact_re.setVisibility(View.GONE);
                    RequestParams params=new RequestParams(Url.FindFriendURL);
                    params.addParameter("keyword",SearchContent);
                    params.addParameter("userid", myUtils.readUser(addFriendActivity_SearchVo.this).getUserid());
                    x.http().post(params, new MyCommonCallback<Result<SearchContactsEntivity>>() {
                        @Override
                        public void success(Result<SearchContactsEntivity> data) {
                            Log.i("ss",data.info);
                            loading_view.setVisibility(View.GONE);
                            Toast.makeText(addFriendActivity_SearchVo.this, data.info, Toast.LENGTH_SHORT).show();
                            SearchContactsEntivity dataEntity=data.data;
                            SearchContactsEntivity.SearchContactsInfo contactsInfo= dataEntity.getInfo();
                            if(contactsInfo!=null){
                                account_info=  contactsInfo.getAccount_info();
                                if(account_info!=null){

                                    if(!TextUtils.isEmpty(account_info.getAccount())){
                                        DemoApplication.accountInfo=account_info;
                                        //显示搜索条目
                                        //Toast.makeText(addFriendActivity_SearchVo.this, "信息="+account_info.getAccount(), Toast.LENGTH_SHORT).show();
                                        result_searchContact_re.setVisibility(View.VISIBLE);
                                        Picasso.with(addFriendActivity_SearchVo.this).load(account_info.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(searchContacts_head);
                                        searchContacts_name.setText("昵称："+account_info.getNickname());
                                        searchContacts_vo.setText("voID: "+account_info.getAccount());
                                    }
                                }else {
                                    result_searchContact_re.setVisibility(View.GONE);
                                  // Toast.makeText(addFriendActivity_SearchVo.this, data.info, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                result_searchContact_re.setVisibility(View.GONE);
                               // Toast.makeText(addFriendActivity_SearchVo.this, data.info, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void error(Throwable ex, boolean isOnCallback) {
                            loading_view.setVisibility(View.GONE);
                            Log.i("err",ex+"s搜索错误");
                        }
                    });
                    return true;
                }
                return false;
            } });
    }

    @OnClick({R.id.iv_back,R.id.result_searchContact_re,R.id.fdj_search})
    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.fdj_search:
                ///*****************************************************************************
                //在这个地方进行搜索
                Utils.downInput(addFriendActivity_SearchVo.this,fdj_search);
                String SearchContent=et_search.getText().toString().trim();
                if(TextUtils.isEmpty(SearchContent)){
                    Toast.makeText(addFriendActivity_SearchVo.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                loading_view.setVisibility(View.VISIBLE);
                result_searchContact_re.setVisibility(View.GONE);

                RequestParams params=new RequestParams(Url.FindFriendURL);
                params.addParameter("keyword",SearchContent);
                params.addParameter("userid", myUtils.readUser(addFriendActivity_SearchVo.this).getUserid());
                x.http().post(params, new MyCommonCallback<Result<SearchContactsEntivity>>() {

                    @Override
                    public void success(Result<SearchContactsEntivity> data) {
                        loading_view.setVisibility(View.GONE);
                       // Toast.makeText(addFriendActivity_SearchVo.this, data.info, Toast.LENGTH_SHORT).show();
                        SearchContactsEntivity dataEntity=data.data;
                        SearchContactsEntivity.SearchContactsInfo contactsInfo= dataEntity.getInfo();
                        if(contactsInfo!=null){
                            account_info=  contactsInfo.getAccount_info();
                            if(account_info!=null){
                                if(!TextUtils.isEmpty(account_info.getAccount())){
                                    DemoApplication.accountInfo=account_info;
                                    //显示搜索条目
                                    //Toast.makeText(addFriendActivity_SearchVo.this, "信息="+account_info.getAccount(), Toast.LENGTH_SHORT).show();
                                    result_searchContact_re.setVisibility(View.VISIBLE);
                                    Picasso.with(addFriendActivity_SearchVo.this).load(account_info.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(searchContacts_head);
                                    searchContacts_name.setText(account_info.getAccount());
                                }
                            }else {
                                result_searchContact_re.setVisibility(View.GONE);
                               // Toast.makeText(addFriendActivity_SearchVo.this, data.info, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            result_searchContact_re.setVisibility(View.GONE);
                           // Toast.makeText(addFriendActivity_SearchVo.this, data.info, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                        Log.i("err",ex+"s搜索错误");
                    }
                });
                //**************************************************************************
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.result_searchContact_re:
                //点击进入好友详情
                Intent intent=new Intent(addFriendActivity_SearchVo.this,UserDetailsActivityVo.class);
                intent.putExtra("HXid",account_info.getHuanxin_account());
                intent.putExtra("way",account_info.getApply_way());
                //是否需要验证
                intent.putExtra("code",account_info.getFriend_apply());
                //账号
                intent.putExtra("account",account_info.getAccount());
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
