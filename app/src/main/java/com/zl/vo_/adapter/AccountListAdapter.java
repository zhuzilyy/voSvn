package com.zl.vo_.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zl.vo_.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class AccountListAdapter extends BaseAdapter {
    //778899
    private Context mContext;
    private List<String> accounts;

    public AccountListAdapter(Context mContext, List<String> accounts) {
        this.mContext = mContext;
        this.accounts = accounts;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(mContext).inflate(R.layout.lay_account_list,null);
        TextView switchAccount_tv=view.findViewById(R.id.switchAccount_tv);
        switchAccount_tv.setText(accounts.get(i));

        return view;
    }
}
