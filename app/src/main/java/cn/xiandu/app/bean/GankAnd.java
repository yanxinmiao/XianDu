package cn.xiandu.app.bean;

import java.util.List;

/**
 * Created by dell on 2016/12/2.
 */

public class GankAnd {


    /**
     * _id : 583e180c421aa939bb4637ca
     * createdAt : 2016-11-30T08:06:36.653Z
     * desc : 支持多个方向滑动的 View，类似 Calendar 里的 UI 效果
     * images : ["http://img.gank.io/ea35e7ac-0d12-474b-b038-ff86d0b0dad3"]
     * publishedAt : 2016-11-30T11:35:55.916Z
     * source : chrome
     * type : Android
     * url : https://github.com/Kelin-Hong/ScrollablePanel
     * used : true
     * who : 嗲马甲
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
    private List<String> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
