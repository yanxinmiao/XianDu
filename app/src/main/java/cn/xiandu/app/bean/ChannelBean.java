package cn.xiandu.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2016/12/2.
 */

public class ChannelBean implements Parcelable {


    /**
     * channelId : 5572a108b3cdc86cf39001cd
     * name : 国内焦点
     */

    private String channelId;
    private String name;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelId);
        dest.writeString(this.name);
    }

    public ChannelBean() {
    }

    protected ChannelBean(Parcel in) {
        this.channelId = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ChannelBean> CREATOR = new Parcelable.Creator<ChannelBean>() {
        @Override
        public ChannelBean createFromParcel(Parcel source) {
            return new ChannelBean(source);
        }

        @Override
        public ChannelBean[] newArray(int size) {
            return new ChannelBean[size];
        }
    };
}
