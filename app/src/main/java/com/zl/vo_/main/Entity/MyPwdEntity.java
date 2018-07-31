package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MyPwdEntity {
    public MyPwdInfo getInfo() {
        return Info;
    }

    public void setInfo(MyPwdInfo info) {
        Info = info;
    }

    private MyPwdInfo Info;
    public class MyPwdInfo{
        public MyPwdCell getPrivacypassinfo() {
            return privacypassinfo;
        }

        public void setPrivacypassinfo(MyPwdCell privacypassinfo) {
            this.privacypassinfo = privacypassinfo;
        }

        public MyPwdCell privacypassinfo;

        public class MyPwdCell{

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

            private String passid;
            private String isset;
            private String ismove;
            private String starttime;


        }

    }
}
