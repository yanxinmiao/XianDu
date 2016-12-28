package cn.xiandu.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by dell on 2016/12/2.
 */
@Entity //用于标识这是一个需要Greendao帮我们生成代码的bean
public class ChannelData implements Parcelable {


    /**
     * allList : ["　　原标题：武钢原董事长邓崎琳被提起公诉（图|简历）","　　中国经济网北京12月8日综合报道 据最高检官网消息，近日，福建省、天津市、广东省检察机关依法对中共上海市委原常委、上海市人民政府原副市长艾宝俊涉嫌受贿、贪污案，中共内蒙古自治区党委原常委、自治区政府原副主席潘逸阳涉嫌受贿、行贿案，华润（集团）有限公司原董事长宋林涉嫌受贿、贪污案，武汉钢铁（集团）公司原董事长邓崎琳涉嫌受贿案提起公诉。","　　武汉钢铁（集团）公司原董事长邓崎琳涉嫌受贿一案，由最高人民检察院指定广东省人民检察院侦查终结后，移送广东省佛山市人民检察院审查起诉。近日，佛山市人民检察院已向佛山市中级人民法院提起公诉。检察机关起诉指控：被告人邓崎琳利用担任武汉钢铁（集团）公司党委常委、副书记、书记、副总经理、总经理、董事长等职务上的便利，为他人谋取利益，非法收受他人巨额财物，依法应当以受贿罪追究其刑事责任。",{"height":259,"width":250,"url":"http://n.sinaimg.cn/translate/20161208/cY3s-fxypipt0554809.jpg"},"　　邓崎琳，男，1951年11月生，湖南津市人。1978年4月加入中国共产党。1975年10月参加工作。研究生学历，硕士学位。教授级高级工程师。","　　1975年10月武汉钢铁学院冶金专业毕业，任武钢炼钢厂车间工艺技术员、冶炼工长、总工长、炼钢车间副主任、主任、生产科科长、副厂长。","　　1986年任第2炼钢厂厂长（1987年晋升为工程师）。","　　1992年4月任武钢集团公司生产部部长。","　　1992年7月任武钢集团公司总经理助理。","　　1995年4月任武钢集团副总经理（1995年晋升为高级工程师）。","　　1999年4月任武钢集团公司党委常委、副总经理（1999年晋升为教授级高级工程师）。","　　2001年8月任武钢集团公司党委常委、副总经理兼任钢铁有限公司总经理。","　　2004年12月任武汉钢铁集团公司总经理、党委副书记。","　　2005年1月任中国钢铁工业协会副会长，武汉钢铁集团公司总经理、党委副书记。","　　2009年2月19日中国钢铁工业协会会长。","　　2013年7月至2015年6月武汉钢铁（集团）公司董事长、党委书记。","　　2015年8月，涉嫌严重违纪违法，接受调查。"]
     * pubDate : 2016-12-08 15:38:24
     * havePic : true
     * title : 武钢原董事长邓崎琳被提起公诉 收受巨额财物
     * channelName : 国内焦点
     * imageurls : [
     * {"height":259,"width":250,"url":"http://n.sinaimg.cn/translate/20161208/cY3s-fxypipt0554809.jpg"}
     * ]
     * desc : 　　原标题：武钢原董事长邓崎琳被提起公诉（图|简历）　　中国经济网北京12月8日综合报道据最高检官网消息，近日，福建省、天津市、广东省检察机关依法对中共上海市委原常委、上海市人民政府原副市长艾宝俊涉嫌受贿、贪污案，中共内蒙古自治区党委原常委、自治区政府原副主席潘....
     * source : 新浪
     * channelId : 5572a108b3cdc86cf39001cd
     * link : http://news.sina.com.cn/o/2016-12-08/doc-ifxypcqa9067764.html
     */
    @Id //添加了 @Id 注解，这个就是主键了
    private Long id ;
    private String pubDate;
    private boolean havePic;
    private String title;
    private String channelName;
    private String desc;
    private String source;
    private String channelId;
    private String link;

    /**
     * height : 259
     * width : 250
     * url : http://n.sinaimg.cn/translate/20161208/cY3s-fxypipt0554809.jpg
     */
    @Transient
    private List<ImageurlsBean> imageurls;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pubDate);
        dest.writeByte(this.havePic ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.channelName);
        dest.writeString(this.desc);
        dest.writeString(this.source);
        dest.writeString(this.channelId);
        dest.writeString(this.link);
    }

    public ChannelData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public boolean isHavePic() {
        return havePic;
    }

    public void setHavePic(boolean havePic) {
        this.havePic = havePic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<ImageurlsBean> getImageurls() {
        return imageurls;
    }

    public void setImageurls(List<ImageurlsBean> imageurls) {
        this.imageurls = imageurls;
    }

    public boolean getHavePic() {
        return this.havePic;
    }

    protected ChannelData(Parcel in) {
        this.pubDate = in.readString();
        this.havePic = in.readByte() != 0;
        this.title = in.readString();
        this.channelName = in.readString();
        this.desc = in.readString();
        this.source = in.readString();
        this.channelId = in.readString();
        this.link = in.readString();
    }

    @Generated(hash = 518826252)
    public ChannelData(Long id, String pubDate, boolean havePic, String title, String channelName, String desc, String source, String channelId, String link) {
        this.id = id;
        this.pubDate = pubDate;
        this.havePic = havePic;
        this.title = title;
        this.channelName = channelName;
        this.desc = desc;
        this.source = source;
        this.channelId = channelId;
        this.link = link;
    }

    public static final Parcelable.Creator<ChannelData> CREATOR = new Parcelable.Creator<ChannelData>() {
        @Override
        public ChannelData createFromParcel(Parcel source) {
            return new ChannelData(source);
        }

        @Override
        public ChannelData[] newArray(int size) {
            return new ChannelData[size];
        }
    };

}
