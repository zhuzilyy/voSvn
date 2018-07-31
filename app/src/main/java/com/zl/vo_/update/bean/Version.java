package com.zl.vo_.update.bean;

import java.io.Serializable;

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
public class Version implements Serializable {
    private String version;
    private String url;
    private String notes;
    private String flag;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
