package com.zl.vo_.main.activities;

/**
 * Created by Administrator on 2018/1/12.
 */

public class ZFBData {
    public ZFBInfo getInfo() {
        return Info;
    }
    public void setInfo(ZFBInfo info) {
        Info = info;
    }
    public ZFBInfo Info;
    public class ZFBInfo{
        public String getOrderstr() {
            return orderstr;
        }
        public void setOrderstr(String orderstr) {
            this.orderstr = orderstr;
        }
        private String orderstr;
    }
}
