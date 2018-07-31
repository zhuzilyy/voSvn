package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PrivacyInfoData {
    public PrivacyInfo getInfo() {
        return Info;
    }

    public void setInfo(PrivacyInfo info) {
        Info = info;
    }

    private PrivacyInfo Info;
    public class PrivacyInfo{

        public PrivacyCell getPrivate_info() {
            return private_info;
        }

        public void setPrivate_info(PrivacyCell private_info) {
            this.private_info = private_info;
        }

        private PrivacyCell private_info;
        public class PrivacyCell{
            public String getPrivate_5() {
                return private_5;
            }

            public void setPrivate_5(String private_5) {
                this.private_5 = private_5;
            }

            public String getPrivate_6() {
                return private_6;
            }

            public void setPrivate_6(String private_6) {
                this.private_6 = private_6;
            }

            public String getPrivate_7() {
                return private_7;
            }

            public void setPrivate_7(String private_7) {
                this.private_7 = private_7;
            }

            public String getPrivate_8() {
                return private_8;
            }

            public void setPrivate_8(String private_8) {
                this.private_8 = private_8;
            }

            public String getPrivate_9() {
                return private_9;
            }

            public void setPrivate_9(String private_9) {
                this.private_9 = private_9;
            }

            private String private_5;
            private String private_6;
            private String private_7;
            private String private_8;
            private String private_9;
        }


    }
}
