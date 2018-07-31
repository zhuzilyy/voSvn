package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */

public class MyPwdList {
    public MyPwdListInfo getInfo() {
        return Info;
    }

    public void setInfo(MyPwdListInfo info) {
        Info = info;
    }

    public MyPwdListInfo Info;
    public class  MyPwdListInfo{


        public List<pwdList> getPrivacypass_list() {
            return privacypass_list;
        }

        public void setPrivacypass_list(List<pwdList> privacypass_list) {
            this.privacypass_list = privacypass_list;
        }

        public List<pwdList> privacypass_list;
        public class pwdList{
            public String getIsmove() {
                return ismove;
            }

            public void setIsmove(String ismove) {
                this.ismove = ismove;
            }

            public String getPassid() {
                return passid;
            }

            public void setPassid(String passid) {
                this.passid = passid;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getIsset() {
                return isset;
            }

            public void setIsset(String isset) {
                this.isset = isset;
            }

            public String passid;
            public String userid;
            public String starttime;
            public String isset;
            public String ismove;

        }
    }
}
