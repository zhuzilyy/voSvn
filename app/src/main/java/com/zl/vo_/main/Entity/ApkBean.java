package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2018/5/26.
 */

public class ApkBean {
    private ApkData data;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ApkData getData() {
        return data;
    }

    public void setData(ApkData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String info;
    private String code;
    public class ApkData{
        private ApkInfo Info;

        public ApkInfo getInfo() {
            return Info;
        }

        public void setInfo(ApkInfo info) {
            Info = info;
        }

        public class ApkInfo{
            private String version;
            private String describe;
            private String downurl;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

    }
}
