package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/12/3.
 */

public class SearchContactsEntivity {
    public SearchContactsInfo getInfo() {
        return Info;
    }

    public void setInfo(SearchContactsInfo info) {
        Info = info;
    }

    public SearchContactsInfo Info;

    public class SearchContactsInfo{
        public SearchContactsAccountInfo getAccount_info() {
            return account_info;
        }

        public void setAccount_info(SearchContactsAccountInfo account_info) {
            this.account_info = account_info;
        }

        public SearchContactsAccountInfo account_info;

        public class SearchContactsAccountInfo{

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getVip() {
                return vip;
            }

            public void setVip(String vip) {
                this.vip = vip;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getApply_account() {
                return apply_account;
            }

            public void setApply_account(String apply_account) {
                this.apply_account = apply_account;
            }

            public String getApply_mobile() {
                return apply_mobile;
            }

            public void setApply_mobile(String apply_mobile) {
                this.apply_mobile = apply_mobile;
            }

            public String getApply_erweima() {
                return apply_erweima;
            }

            public void setApply_erweima(String apply_erweima) {
                this.apply_erweima = apply_erweima;
            }

            public String getApply_group() {
                return apply_group;
            }

            public void setApply_group(String apply_group) {
                this.apply_group = apply_group;
            }

            public String getApply_card() {
                return apply_card;
            }

            public void setApply_card(String apply_card) {
                this.apply_card = apply_card;
            }

            public String getFriend_check() {
                return friend_check;
            }

            public void setFriend_check(String friend_check) {
                this.friend_check = friend_check;
            }

            private String id;
            private String userid;
            private String account;
            private String mobile;
            private String avatar;
            private String vip;
            private String sex;
            private String address;
            private String nickname;
            private String apply_account;
            private String apply_mobile;
            private String apply_erweima;
            private String apply_group;
            private String apply_card;
            private String friend_check;
            private String apply_way;
            private String friend_apply;
            private String huanxin_account;

            public String getHuanxin_account() {
                return huanxin_account;
            }

            public void setHuanxin_account(String huanxin_account) {
                this.huanxin_account = huanxin_account;
            }

            public String getFriend_apply() {
                return friend_apply;
            }

            public void setFriend_apply(String friend_apply) {
                this.friend_apply = friend_apply;
            }

            public String getApply_way() {
                return apply_way;
            }

            public void setApply_way(String apply_way) {
                this.apply_way = apply_way;
            }
        }

    }

}
