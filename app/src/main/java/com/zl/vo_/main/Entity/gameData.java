package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class gameData {
    public gameInfo getInfo() {
        return Info;
    }

    public void setInfo(gameInfo info) {
        Info = info;
    }

    private gameInfo Info;
    public class gameInfo{
        public List<gameCell> getPackage_list() {
            return package_list;
        }

        public void setPackage_list(List<gameCell> package_list) {
            this.package_list = package_list;
        }

        private List<gameCell> package_list;
        public class gameCell{
            public String getPackname() {
                return packname;
            }

            public void setPackname(String packname) {
                this.packname = packname;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }

            public String getPicurl() {
                return picurl;
            }

            public void setPicurl(String picurl) {
                this.picurl = picurl;
            }

            public String getPackdescription() {
                return packdescription;
            }

            public void setPackdescription(String packdescription) {
                this.packdescription = packdescription;
            }

            private String packname;
            private String downurl;
            private String picurl;
            private String packdescription;
            private String apkname;
            private String sign;
            private String gamename;

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getGamename() {
                return gamename;
            }

            public void setGamename(String gamename) {
                this.gamename = gamename;
            }

            public String getApkname() {
                return apkname;
            }

            public void setApkname(String apkname) {
                this.apkname = apkname;
            }
        }

    }
}
