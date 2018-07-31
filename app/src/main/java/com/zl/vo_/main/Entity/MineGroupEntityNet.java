package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/12/9.
 */

public class MineGroupEntityNet {
    public MineGroupInfo getInfo() {
        return Info;
    }

    public void setInfo(MineGroupInfo info) {
        Info = info;
    }

    private MineGroupInfo Info;
    public class MineGroupInfo{
        public MineGroupInfoArr getGroupinfo() {
            return groupinfo;
        }

        public void setGroupinfo(MineGroupInfoArr groupinfo) {
            this.groupinfo = groupinfo;
        }

        private MineGroupInfoArr groupinfo;
        public class MineGroupInfoArr{
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

            private String groupid;
            private String groupname;

        }

    }
}
