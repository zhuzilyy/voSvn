package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2018/2/2.
 */

public class WxPayData {
    public WxPayInfo getInfo() {
        return Info;
    }

    public void setInfo(WxPayInfo info) {
        Info = info;
    }

    public WxPayInfo Info;
    public class WxPayInfo{
        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getNotify_url() {
            return notify_url;
        }

        public void setNotify_url(String notify_url) {
            this.notify_url = notify_url;
        }

        private String total_fee;
        private String out_trade_no;
        private String appid;
        private String mch_id;
        private String key;
        private String notify_url;

    }
}
