package com.zl.vo_.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SwitchAccountDao {
    /***
     * 保存一个分组
     */

    public boolean saveSwitchAccount(String account){
        DemoDBManager dbManager=DemoDBManager.getInstance();
        try {
            dbManager.saveAccountSwitch(account);
            return true;
        }catch (Exception e){
            return false;

        }

    }

    /****
     * 查询所有的登录过账号
     */

    public List<String> getSwitAccount(){
        DemoDBManager dbManager=DemoDBManager.getInstance();

        try{
            Log.i("switch","查询成功 大小=="+dbManager.selectAccount().size());
            return dbManager.selectAccount();


        }catch (Exception e){
            Log.i("switch","查询失败== 大小==");
            return null;

        }

    }

    /*****
     * 删除账号
     *
     */

    public boolean deleteSwitchAccount(String account){
        DemoDBManager dbManager=DemoDBManager.getInstance();
        try{
            dbManager.deleteSwitchAccount(account);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    /****
     * 查询一个账号
     */
public String getAccount(String account){
    DemoDBManager dbManager=DemoDBManager.getInstance();
    try {
        String AccountStr=dbManager.getSwitchAccount(account);
        return AccountStr;
    }catch (Exception e){
        e.printStackTrace();
        return null;
    }
}

}
