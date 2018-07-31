package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */

public class UserListData {


    public UserListInfo getInfo() {
        return Info;
    }

    public void setInfo(UserListInfo info) {
        Info = info;
    }

    public UserListInfo Info;
    public class UserListInfo{

        public List<UserListCell> getAccount_info_list() {
            return account_info_list;
        }

        public void setAccount_info_list(List<UserListCell> account_info_list) {
            this.account_info_list = account_info_list;
        }

        List<UserListCell> account_info_list;
            public class UserListCell{

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                private String avatar;
                private String nickname;

                public String getHuanxin_account() {
                    return huanxin_account;
                }

                public void setHuanxin_account(String huanxin_account) {
                    this.huanxin_account = huanxin_account;
                }

                private String huanxin_account;


            }

    }
}
