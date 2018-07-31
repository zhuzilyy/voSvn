package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class LifeNoteListData {
    public LifeNoteListInfo getInfo() {
        return Info;
    }

    public void setInfo(LifeNoteListInfo info) {
        Info = info;
    }

    private LifeNoteListInfo Info;

    public class LifeNoteListInfo{

        public List<LifeNoteListList> getPerson_record_list() {
            return person_record_list;
        }

        public void setPerson_record_list(List<LifeNoteListList> person_record_list) {
            this.person_record_list = person_record_list;
        }

        private List<LifeNoteListList> person_record_list;

        public class LifeNoteListList{

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<String> getPicarr() {
                return picarr;
            }

            public void setPicarr(List<String> picarr) {
                this.picarr = picarr;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            private String id;
            private String content;
            private List<String> picarr;
            private String addtime;


        }

    }
}
