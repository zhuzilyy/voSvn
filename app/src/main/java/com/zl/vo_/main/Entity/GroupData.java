package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class GroupData {
    public GroupInfo getInfo() {
        return Info;
    }

    public void setInfo(GroupInfo info) {
        Info = info;
    }

    private GroupInfo Info;
    public class GroupInfo{
        public List<GrouopList> getGrouplist() {
            return grouplist;
        }

        public void setGrouplist(List<GrouopList> grouplist) {
            this.grouplist = grouplist;
        }

        private List<GrouopList> grouplist;
        public class GrouopList{
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
