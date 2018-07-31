package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/11/30.
 */

public class adEntivity {
    public adInfo getInfo() {
        return Info;
    }

    public void setInfo(adInfo info) {
        Info = info;
    }

    private adInfo Info;

    public class adInfo{

        public adImage getIndex_image() {
            return index_image;
        }

        public void setIndex_image(adImage index_image) {
            this.index_image = index_image;
        }

        private adImage index_image;
        public class adImage{
            public String getPicurl() {
                return picurl;
            }

            public void setPicurl(String picurl) {
                this.picurl = picurl;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            private String picurl;
            private String link;
            private String time;

        }
    }
}
