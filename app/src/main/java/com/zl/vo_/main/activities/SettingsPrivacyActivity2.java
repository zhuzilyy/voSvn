package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zl.vo_.R;
import com.zl.vo_.main.views.XTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/1/24.
 */

public class SettingsPrivacyActivity2 extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.title)
    public XTitleView title;
    @BindView(R.id.privacy2_set)
    public Button btn_set;
    @BindView(R.id.privacy2_fix)
    public Button btn_fix;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_settings_pravacy2);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @OnClick({R.id.privacy2_set,R.id.privacy2_fix})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.privacy2_set:
                startActivity(new Intent(SettingsPrivacyActivity2.this,SettingsPrivacyActivity3.class));
            break;
            case R.id.privacy2_fix:
                startActivity(new Intent(SettingsPrivacyActivity2.this,SettingsPrivacyActivity4.class));
                break;
            default:
            break;


        }
    }
}
