package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class CheckPwdEntivity {
    public CheckPwdInfo getInfo() {
        return Info;
    }

    public void setInfo(CheckPwdInfo info) {
        Info = info;
    }

    public CheckPwdInfo Info;

    public class CheckPwdInfo{

        public CheckPwd_info getPrivacypassinfo() {
            return privacypassinfo;
        }

        public void setPrivacypassinfo(CheckPwd_info privacypassinfo) {
            this.privacypassinfo = privacypassinfo;
        }

        public List<CheckPwdFriendList> getFriend_list() {
            return friend_list;
        }

        public void setFriend_list(List<CheckPwdFriendList> friend_list) {
            this.friend_list = friend_list;
        }

        public CheckPwd_info privacypassinfo;
        public List<CheckPwdFriendList> friend_list;


        public class CheckPwd_info{
            public String getPassid() {
                return passid;
            }

            public void setPassid(String passid) {
                this.passid = passid;
            }

            public String getIsset() {
                return isset;
            }

            public void setIsset(String isset) {
                this.isset = isset;
            }

            public String getIsmove() {
                return ismove;
            }

            public void setIsmove(String ismove) {
                this.ismove = ismove;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getNexttime() {
                return nexttime;
            }

            public void setNexttime(String nexttime) {
                this.nexttime = nexttime;
            }

            private String passid;
            private String isset;
            private String ismove;
            private String starttime;
            private String nexttime;

        }

        public class CheckPwdFriendList{
            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getPassid() {
                return passid;
            }

            public void setPassid(String passid) {
                this.passid = passid;
            }

            private String userid;
            private String passid;
        }


    }

}
