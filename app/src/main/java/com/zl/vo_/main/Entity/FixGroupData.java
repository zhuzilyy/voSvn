package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/12/10.
 */

public class FixGroupData {
    public FixGroupInfo getInfo() {
        return Info;
    }

    public void setInfo(FixGroupInfo info) {
        Info = info;
    }

    public FixGroupInfo Info;
    public class  FixGroupInfo{
        public FixGroupCell getGroup_info() {
            return group_info;
        }

        public void setGroup_info(FixGroupCell group_info) {
            this.group_info = group_info;
        }

        private FixGroupCell group_info;
        public class FixGroupCell{
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
