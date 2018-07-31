package com.zl.vo_.main.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zl.vo_.R;
import com.zl.vo_.db.MyFriendGroupDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 */

public class TestDBMGroup_ extends Activity implements View.OnClickListener{
    @BindView(R.id.ed_001)
    public EditText et_001;
    @BindView(R.id.ed_002)
    public EditText et_002;
    @BindView(R.id.btn_add)
    public Button btn_add;
    @BindView(R.id.btn_del)
    public Button btn_del;
    @BindView(R.id.btn_select)
    public Button btn_select;
    @BindView(R.id.btn_update)
    public Button btn_update;
    private  MyFriendGroupDao myFriendGroupDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.lay_test_mmgroup);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();

    }

    private void mInit() {
        myFriendGroupDao=new MyFriendGroupDao();

    }
    @OnClick({R.id.btn_add,R.id.btn_del,R.id.btn_update,R.id.btn_select})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add:
               String name= et_001.getText().toString().trim();
               // myFriendGroupDao.saveMyGroup(name);

                break;
            case R.id.btn_del:
               // String name2= et_001.getText().toString().trim();
               // myFriendGroupDao.deleteMyGroup(name2);
                break;
            case R.id.btn_update:
               // myFriendGroupDao.updateMyGroup(2,"hyp&xzy");
                break;
            case R.id.btn_select:
               // List<MyFriendGroupEntity> groupEntities= myFriendGroupDao.selectGroupEntities();
              //  Log.i("uu","大小：=="+groupEntities.size());
                break;
            default:
                break;
        }
    }
}
