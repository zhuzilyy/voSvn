package com.zl.vo_.main.Entity;

import java.net.PortUnreachableException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class FriendListEntity {

    public FriendListInfo getInfo() {
        return Info;
    }

    public void setInfo(FriendListInfo info) {
        Info = info;
    }

    private FriendListInfo Info;



    public class FriendListInfo{

        public List<FriendListInfo.friend_list> getFriend_list() {
            return friend_list;
        }

        public void setFriend_list(List<FriendListInfo.friend_list> friend_list) {
            this.friend_list = friend_list;
        }

        private List<friend_list> friend_list;



        public class friend_list{

            private String id;
            private String userid;
            private String huanxin_account;
            private String avatar;
            private String vip;
            private String sex;
            private String address;
            private String nickname;
            private String account;
            private String mobile;
            private String remark;
            private String groupid;
            private String groupname;
            private String passid;
            private String friend_check;

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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getGroupid() {
                return groupid;
            }

            public void setGroupid(String groupid) {
                this.groupid = groupid;
            }

            public String getGroupname() {
                return groupname;
            }

            public void setGroupname(String groupname) {
                this.groupname = groupname;
            }

            public String getPassid() {
                return passid;
            }

            public void setPassid(String passid) {
                this.passid = passid;
            }

            public String getFriend_check() {
                return friend_check;
            }

            public void setFriend_check(String friend_check) {
                this.friend_check = friend_check;
            }
        }

    }
}
