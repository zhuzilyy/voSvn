package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/12/26.
 */

public class GruoupContactData {
    public GruoupContactInfo Info;

    public GruoupContactInfo getInfo() {
        return Info;
    }

    public void setInfo(GruoupContactInfo info) {
        Info = info;
    }

    public class GruoupContactInfo{
        public GroupInfo group_info;
        public ContactInfo account_info;

        public GroupInfo getGroup_info() {
            return group_info;
        }

        public void setGroup_info(GroupInfo group_info) {
            this.group_info = group_info;
        }

        public ContactInfo getAccount_info() {
            return account_info;
        }

        public void setAccount_info(ContactInfo account_info) {
            this.account_info = account_info;
        }

        public class GroupInfo{
            private String id;
            private String name;
            private String description;
            private String membersionly;
            private String allowinvites;
            private String maxusers;
            private String owner;
            private String created;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getMembersionly() {
                return membersionly;
            }

            public void setMembersionly(String membersionly) {
                this.membersionly = membersionly;
            }

            public String getAllowinvites() {
                return allowinvites;
            }

            public void setAllowinvites(String allowinvites) {
                this.allowinvites = allowinvites;
            }

            public String getMaxusers() {
                return maxusers;
            }

            public void setMaxusers(String maxusers) {
                this.maxusers = maxusers;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }
        }
        public class ContactInfo{
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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            private String userid;
            private String huanxin_account;
            private String avatar;
            private String address;
            private String nickname;
            private String remark;
            private String account;
        }
    }
}
