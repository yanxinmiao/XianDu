package cn.xiandu.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dell on 2016/11/30.
 */
@Entity //用于标识这是一个需要Greendao帮我们生成代码的bean
public class HomeData implements Parcelable {


    /**
     * ctime : 2016-03-31
     * title : 总说韩国艺人来中国捞金，先看完这些再说吧
     * description : RunningMan
     * picUrl : http://zxpic.gtimg.com/infonew/0/wechat_pics_-4225344.jpg/640
     * url : http://mp.weixin.qq.com/s?__biz=MzA3NDcwNzUxMw==&idx=4&mid=403101020&sn=5a9fa497df693b768637faf85f2b6449
     */
    @Id //添加了 @Id 注解，这个就是主键了
    private Long id;
    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override

    public String toString() {
        return "HomeData{" +
                "ctime='" + ctime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ctime);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.picUrl);
        dest.writeString(this.url);
    }

    protected HomeData(Parcel in) {
        this.ctime = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.picUrl = in.readString();
        this.url = in.readString();
    }

    @Generated(hash = 588811232)
    public HomeData(Long id, String ctime, String title, String description, String picUrl, String url) {
        this.id = id;
        this.ctime = ctime;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.url = url;
    }

    @Generated(hash = 514576371)
    public HomeData() {
    }

    public static final Parcelable.Creator<HomeData> CREATOR = new Parcelable.Creator<HomeData>() {
        @Override
        public HomeData createFromParcel(Parcel source) {
            return new HomeData(source);
        }

        @Override
        public HomeData[] newArray(int size) {
            return new HomeData[size];
        }
    };
}
