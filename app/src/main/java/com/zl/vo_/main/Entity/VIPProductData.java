package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class VIPProductData {
    public VIPProductInfo getInfo() {
        return Info;
    }

    public void setInfo(VIPProductInfo info) {
        Info = info;
    }

    private VIPProductInfo Info;

    public class VIPProductInfo{
        public List<VIPProductCell> getVipproduct_list() {
            return vipproduct_list;
        }

        public void setVipproduct_list(List<VIPProductCell> vipproduct_list) {
            this.vipproduct_list = vipproduct_list;
        }

        private List<VIPProductCell> vipproduct_list;

        public class VIPProductCell{

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getLimit() {
                return limit;
            }

            public void setLimit(String limit) {
                this.limit = limit;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            private String product_id;
            private String limit;
            private String price;
            private String discount;
            private String discount_price;

            public String getDiscount_price() {
                return discount_price;
            }

            public void setDiscount_price(String discount_price) {
                this.discount_price = discount_price;
            }
        }

    }
}
