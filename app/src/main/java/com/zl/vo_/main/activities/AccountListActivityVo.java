package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.zl.vo_.R;
import com.zl.vo_.adapter.AccountListAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.SwitchAccountDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/5.
 */

public class AccountListActivityVo extends VoBaseActivity {
  @BindView(R.id.accontlv)
    public ListView accontlv;
    private AccountListAdapter adapter;
    private List<String> BigAccounts=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_switch_account);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {

        DemoDBManager dbManager=DemoDBManager.getInstance();
        List<String> accounts=dbManager.selectAccount();
        if(accounts.size()>0){
            BigAccounts.clear();
            BigAccounts.addAll(accounts);
            adapter=new AccountListAdapter(AccountListActivityVo.this,BigAccounts);
            accontlv.setAdapter(adapter);
        }

        accontlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("account",BigAccounts.get(i));
                setResult(200,intent);
                finish();
            }
        });
        accontlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                DialogUIUtils.showMdAlert(AccountListActivityVo.this, "删除账号", "是否删除该账号", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        SwitchAccountDao dao=new SwitchAccountDao();
                        boolean b = dao.deleteSwitchAccount(BigAccounts.get(i));
                        if(b){
                            DemoDBManager dbManager=DemoDBManager.getInstance();
                            List<String> Accounts=dbManager.selectAccount();
                            BigAccounts.clear();
                            BigAccounts.addAll(Accounts);
                            adapter.notifyDataSetChanged();


                            Toast.makeText(AccountListActivityVo.this, "删除成功", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onNegative() {


                    }
                }).show();

                return true;
            }
        });






    }
}
