package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/11/28.
 */

public class WXErrorInfo {
    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    private String errcode;
    private String errmsg;
}
