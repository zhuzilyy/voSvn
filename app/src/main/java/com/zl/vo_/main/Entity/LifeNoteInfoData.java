package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class LifeNoteInfoData {
    public LifeNoteInfo getInfo() {
        return Info;
    }

    public void setInfo(LifeNoteInfo info) {
        Info = info;
    }

    public LifeNoteInfo Info;

    public class LifeNoteInfo{
        public LifeNoteDetails getPerson_record_info() {
            return person_record_info;
        }

        public void setPerson_record_info(LifeNoteDetails person_record_info) {
            this.person_record_info = person_record_info;
        }

        private LifeNoteDetails person_record_info;

        public class LifeNoteDetails{
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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public List<String> getPicarr() {
                return picarr;
            }

            public void setPicarr(List<String> picarr) {
                this.picarr = picarr;
            }

            private String id;
            private String userid;
            private String content;
            private String addtime;
            private List<String> picarr;

        }
    }
}
