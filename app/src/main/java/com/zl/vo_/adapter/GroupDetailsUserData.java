package com.zl.vo_.adapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public class GroupDetailsUserData {
    public GroupDetailsUserInfo getInfo() {
        return Info;
    }

    public void setInfo(GroupDetailsUserInfo info) {
        Info = info;
    }

    private GroupDetailsUserInfo Info;

    public class GroupDetailsUserInfo{

        public List<GroupDetailsMemberCell> getMember_list() {
            return member_list;
        }

        public void setMember_list(List<GroupDetailsMemberCell> member_list) {
            this.member_list = member_list;
        }

        public List<GroupDetailsOwerCell> getOwner_list() {
            return owner_list;
        }

        public void setOwner_list(List<GroupDetailsOwerCell> owner_list) {
            this.owner_list = owner_list;
        }

        private List<GroupDetailsMemberCell> member_list;
        private List<GroupDetailsOwerCell> owner_list;



        public class GroupDetailsMemberCell{

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

            private String huanxin_account;
            private String avatar;
            private String vip;
            private String address;
            private String nickname;


        }

        public class GroupDetailsOwerCell{

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

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            private String huanxin_account;
            private String avatar;
            private String vip;
            private String nickname;

        }


    }


}
