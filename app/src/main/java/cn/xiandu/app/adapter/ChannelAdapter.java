package cn.xiandu.app.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.ChannelData;
import cn.xiandu.app.utils.ImageUtils;
import cn.xiandu.app.utils.ThemeManager;

/**
 * Created by dell on 2016/12/2.
 */

public class ChannelAdapter extends BaseQuickAdapter<ChannelData> {
    private Context context ;
    public ChannelAdapter(Context context ,int layoutResId, List<ChannelData> data) {
        super(layoutResId, data);
        this.context = context ;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ChannelData channelData) {
//        baseViewHolder.getItemViewType();

        baseViewHolder.setText(R.id.tv_desc,  channelData.getSource());
        baseViewHolder.setText(R.id.tv_title,channelData.getTitle());
        baseViewHolder.setText(R.id.tv_time,channelData.getPubDate().substring(0,channelData.getPubDate().length() - 3));

        baseViewHolder.setText(R.id.tv_descdd,channelData.getDesc());

        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image);
        if (channelData.isHavePic()){
            baseViewHolder.setVisible(R.id.image,true);
            final String image = channelData.getImageurls().get(0).getUrl();
            ImageUtils.loadImage(context,image ,imageView);
            baseViewHolder.addOnClickListener(R.id.image);
        }else{
            baseViewHolder.setVisible(R.id.image,false);
            imageView.setOnClickListener(null);
            imageView.setImageResource(R.drawable.default_news_cat_pic);
        }
        RelativeLayout ll = (RelativeLayout) baseViewHolder.getConvertView().findViewById(R.id.rl_channel);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_title);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
        TextView tvDesc = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_desc);
        tvDesc.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_666666)));
        TextView tvDescdd = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_descdd);
        tvDescdd.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_999999)));
        TextView tvTime = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_time);
        tvTime.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_999999)));
    }
}
