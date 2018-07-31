package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2018/5/25.
 */

public class CommentBean {
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    private String info;
    private String code;
    private CommentData data;
    public class CommentData{
        public CommentInfo getInfo() {
            return Info;
        }

        public void setInfo(CommentInfo info) {
            Info = info;
        }

        private CommentInfo Info;
        public class CommentInfo{

        }
    }
}
