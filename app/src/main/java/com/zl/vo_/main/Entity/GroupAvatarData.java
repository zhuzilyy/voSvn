package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2018/1/4.
 */

public class GroupAvatarData {
    public GroupAvatarInfo getInfo() {
        return Info;
    }

    public void setInfo(GroupAvatarInfo info) {
        Info = info;
    }

    public GroupAvatarInfo Info;
    public class GroupAvatarInfo{
        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        private String img_url;
    }
}
