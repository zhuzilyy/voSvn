package com.zl.vo_.update.bean;

/**
 * ============================================================
 * <p>
 * 版 权 ： 沈阳夜鱼科技有限公司
 * <p>
 * 作 者 ： LMY
 * <p>
 * 版 本 ： 2.0
 * <p>
 * 创建日期 ：2017.09.07 19:41
 * <p>
 * 描 述 ：
 * 首页视频列表
 * <p>
 * 修订历史 ：
 * <p>
 * ============================================================
 **/
public class VideoBean {
    private String id;
    private String title;
    private String url;
    private String img;
    private String readNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }
}
