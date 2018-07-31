package com.zl.vo_.update.bean;

/**
 * ============================================================
 * <p>
 * 版 权 ： 沈阳夜鱼科技有限公司
 * <p>
 * 作 者 ： LMY
 * <p>
 * 版 本 ： 2.0
 * <p>
 * 创建日期 ：2017.09.22 16:16
 * <p>
 * 描 述 ：
 * 版本信息
 * <p>
 * 修订历史 ：
 * <p>
 * ============================================================
 **/
public class VersionResult {
    private String type;

    private String code;

    private String message;

    private Version data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Version getData() {
        return data;
    }

    public void setData(Version data) {
        this.data = data;
    }
}
