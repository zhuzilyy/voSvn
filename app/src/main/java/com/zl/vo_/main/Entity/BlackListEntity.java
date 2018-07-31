package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class BlackListEntity {
    public BlackInfo getInfo() {
        return Info;
    }

    public void setInfo(BlackInfo info) {
        Info = info;
    }

    public BlackInfo Info;
    public class  BlackInfo{

        public List<blackListCell> getBlack_list() {
            return black_list;
        }

        public void setBlack_list(List<blackListCell> black_list) {
            this.black_list = black_list;
        }

        public List<blackListCell> black_list;
        public class blackListCell{

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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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

            private String id;
            private  String userid;
            private String avatar;
            private String nickname;
            private String remark;

        }
    }
}
