package com.zl.vo_.main.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.google.gson.Gson;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.bean.JsonBean;
import com.zl.vo_.main.addressUtils.LoginDialogFragment;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.GetJsonDataUtil;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.json.JSONArray;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class FixMoreActivityVo extends VoBaseActivity implements View.OnClickListener,LoginDialogFragment.LoginInputListener{
    @BindView(R.id.re_sex)
    public RelativeLayout re_sex;
    @BindView(R.id.re_address)
    public RelativeLayout re_address;
    @BindView(R.id.tv_sex)
    public TextView tv_sex;
    @BindView(R.id.tv_address)
    public TextView tv_address;
    @BindView(R.id.re_sign)
    public RelativeLayout re_sign;
    @BindView(R.id.tv_save)
    public TextView tv_save;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @BindView(R.id.tv_person_sign)
    public TextView tv_person_sign;
    //--------------------------------------
    public String currentSex;
    public String currendAddress;
    //-------------------------------

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_fixmore);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(FixMoreActivityVo.this);

        if(currentUser!=null){
            tv_sex.setText(currentUser.getSex());
            tv_address.setText(currentUser.getAddress());
            tv_person_sign.setText(currentUser.getSignature());
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    break;
            }
        }
    };
    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }



    @OnClick({R.id.re_sex,R.id.re_address,R.id.re_sign,R.id.tv_save})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.re_sex:
                //  Toast.makeText(this, "性别", Toast.LENGTH_SHORT).show();
                List<String> string_sex = new ArrayList<>();
                string_sex.add("男");
                string_sex.add("女");
                DialogUIUtils.showBottomSheetAndCancel(FixMoreActivityVo.this, string_sex, "取消", new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        switch(position){
                            case 0:
                                tv_sex.setText("男");
                                currentSex="1";

                                break;
                            case 1:
                                tv_sex.setText("女");
                                currentSex="0";
                                break;
                            default:
                                break;
                        }

                    }

                    @Override
                    public void onBottomBtnClick() {
                    }
                }).show();


                break;
            case R.id.re_address:
                //地址
                //地区
               // showLoginDialog();
                if (isLoaded) {
                    showPickerView();
                } else {
                   // Toast.makeText(JsonDataActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.re_sign:
                Intent intent=new Intent(FixMoreActivityVo.this,PersonSignDetailsActivityVo.class);
                startActivityForResult(intent,102);
                break;
            case R.id.tv_save:
                //保存

                loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.UpdateBaseInfoUrl);
                params.addParameter("userid",myUtils.readUser(FixMoreActivityVo.this).getUserid());
                params.addParameter("sex",currentSex);
                params.addParameter("address",tv_address.getText().toString().trim());

                x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
                    @Override
                    public void success(Result<LoginData> data) {
                        loading_view.setVisibility(View.GONE);
                        LoginData loginData=data.data;
                        LoginData.LoginInfo loginInfo=loginData.getInfo();
                        if(loginInfo!=null){
                            LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                            if(user!=null){
                                Log.i("uu","pp=="+user.getAvatar());


                                //清除本地保存的用户信息
                                myUtils.clearSharedUser(FixMoreActivityVo.this);
                                myUtils.saveUser(user,FixMoreActivityVo.this);
                                String hh= myUtils.readUser(FixMoreActivityVo.this).getAvatar();
                                Log.i("pp","更多"+hh);
                                sendBroadcast(new Intent("FixPersonInfoOK"));
                                finish();


                            }
                        }
                    }

                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                    }
                });

                break;
            default:
                break;
        }
    }

    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                tv_address.setText(tx);
              //  Toast.makeText(FixMoreActivityVo.this, tx+"xzz", Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showLoginDialog() {
        LoginDialogFragment dialog = new LoginDialogFragment();
        dialog.show(getFragmentManager(), "loginDialog");
    }
    /****
     * 选择地区
     * @param username
     */
    @Override
    public void onLoginInputComplete(String username) {

        // Toast.makeText(Resource_personal.this,username,Toast.LENGTH_SHORT).show();
      //  tv_address.setText(username);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 200:
                String sign=data.getStringExtra("sign");
                if(!TextUtils.isEmpty(sign)){
                    tv_person_sign.setText(sign);
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


}
