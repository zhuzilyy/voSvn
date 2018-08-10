package com.zl.vo_.main.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/2.
 */

public class LoginData {
    public LoginInfo Info;

    public LoginInfo getInfo() {
        return Info;
    }

    public void setInfo(LoginInfo info) {
        Info = info;
    }

    public class LoginInfo {
        public LoginAccountInfo account_info;

        public LoginAccountInfo getAccount_info() {
            return account_info;
        }

        public void setAccount_info(LoginAccountInfo account_info) {
            this.account_info = account_info;
        }

        public class LoginAccountInfo implements Serializable {


            private String id;
            private String userid;
            private String huanxin_account;
            private String avatar;
            private String vip;
            private String sex;
            private String address;
            private String personpass;
            private String logintime;
            private String loginip;
            private String registertime;
            private String registerip;
            private String nickname;
            private String checkinfo;
            private String delstate;
            private String apply_account;
            private String apply_mobile;
            private String apply_erweima;
            private String apply_group;
            private String apply_card;
            private String openid;
            private String unionid;
            private String friend_apply;
            private String signature;
            private String person_record_backpic;
            private String qrcode;
            private String vip_endtime;
            private String set_account;
            private String personkey;
            private String row_number;
            private String account;
            private String mobile;
            private String sy;

            public String getSy() {
                return sy;
            }

            public void setSy(String sy) {
                this.sy = sy;
            }

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

            public String getPersonpass() {
                return personpass;
            }

            public void setPersonpass(String personpass) {
                this.personpass = personpass;
            }

            public String getLogintime() {
                return logintime;
            }

            public void setLogintime(String logintime) {
                this.logintime = logintime;
            }

            public String getLoginip() {
                return loginip;
            }

            public void setLoginip(String loginip) {
                this.loginip = loginip;
            }

            public String getRegistertime() {
                return registertime;
            }

            public void setRegistertime(String registertime) {
                this.registertime = registertime;
            }

            public String getRegisterip() {
                return registerip;
            }

            public void setRegisterip(String registerip) {
                this.registerip = registerip;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getCheckinfo() {
                return checkinfo;
            }

            public void setCheckinfo(String checkinfo) {
                this.checkinfo = checkinfo;
            }

            public String getDelstate() {
                return delstate;
            }

            public void setDelstate(String delstate) {
                this.delstate = delstate;
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

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getUnionid() {
                return unionid;
            }

            public void setUnionid(String unionid) {
                this.unionid = unionid;
            }

            public String getFriend_apply() {
                return friend_apply;
            }

            public void setFriend_apply(String friend_apply) {
                this.friend_apply = friend_apply;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getPerson_record_backpic() {
                return person_record_backpic;
            }

            public void setPerson_record_backpic(String person_record_backpic) {
                this.person_record_backpic = person_record_backpic;
            }

            public String getQrcode() {
                return qrcode;
            }

            public void setQrcode(String qrcode) {
                this.qrcode = qrcode;
            }

            public String getVip_endtime() {
                return vip_endtime;
            }

            public void setVip_endtime(String vip_endtime) {
                this.vip_endtime = vip_endtime;
            }

            public String getSet_account() {
                return set_account;
            }

            public void setSet_account(String set_account) {
                this.set_account = set_account;
            }

            public String getPersonkey() {
                return personkey;
            }

            public void setPersonkey(String personkey) {
                this.personkey = personkey;
            }

            public String getRow_number() {
                return row_number;
            }

            public void setRow_number(String row_number) {
                this.row_number = row_number;
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
//
        }
    }

}
