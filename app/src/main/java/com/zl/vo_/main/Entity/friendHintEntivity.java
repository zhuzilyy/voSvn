package com.zl.vo_.main.Entity;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */

public class friendHintEntivity  {
    public friendHintInfo getInfo() {
        return Info;
    }

    public void setInfo(friendHintInfo info) {
        Info = info;
    }

    private friendHintInfo Info;
    public class friendHintInfo{

        public List<friend_hintArr> getFriend_hint_arr() {
            return friend_hint_arr;
        }

        public void setFriend_hint_arr(List<friend_hintArr> friend_hint_arr) {
            this.friend_hint_arr = friend_hint_arr;
        }

        List<friend_hintArr> friend_hint_arr;
        public class friend_hintArr {
            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            private String userid;
            private String nickname;
            private String account;
            private String mobile;
            private String tag;
            private String avatar;
            private String huanxin_account;

            public String getHuanxin_account() {
                return huanxin_account;
            }

            public void setHuanxin_account(String huanxin_account) {
                this.huanxin_account = huanxin_account;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
