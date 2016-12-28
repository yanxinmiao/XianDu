package cn.xiandu.app.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.HomeData;
import cn.xiandu.app.utils.ThemeManager;

/**
 * Created by dell on 2016/11/30.
 */

public class HomeAdapter extends BaseQuickAdapter<HomeData> {

    private Context context ;
    public HomeAdapter(Context context ,int layoutResId, List<HomeData> data) {
        super(layoutResId, data);
        this.context = context ;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final HomeData homeData) {
        baseViewHolder.setText(R.id.tv_desc, homeData.getDescription())
            .addOnClickListener(R.id.tv_desc);
        baseViewHolder.setText(R.id.tv_title,homeData.getTitle());
        baseViewHolder.setText(R.id.tv_time,homeData.getCtime());
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image);
        Glide.with(context)//如果this 是activity，当activity销毁时，Glide 将会自动停止请求。如果target 是独立于应用的 activity 生命周期，这时就要传application
                .load(homeData.getPicUrl())//source 可以是网络、资源文件、SD中图片文件和Uri,可以加载gif格式)
//                .bitmapTransform(new CenterCrop(context),new RoundedCornersTransformation(mContext, CommonTool.dip2px(5), 0,
//                        RoundedCornersTransformation.CornerType.TOP_LEFT),new RoundedCornersTransformation(mContext, CommonTool.dip2px(5), 0,
//                        RoundedCornersTransformation.CornerType.TOP_RIGHT))//
                .placeholder(R.drawable.default_news_cat_pic)//占位符
                .error(R.drawable.default_news_cat_pic)//错误占位符,error()接受的参数只能是已经初始化的 drawable 对象或者指明它的资源(R.drawable.<drawable-keyword>)。
                .crossFade()//淡入淡出动画,int duration 可以设置动画时间
                .centerCrop()
//                .skipMemoryCache(true)//不保存到内存
//                .listener(requestListener)

                /**
                 *  DiskCacheStrategy.NONE 什么都不缓存
                 DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。在我们上面的例子中，将会只有一个 1000x1000 像素的图片
                 DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）
                 DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
                 */
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//不保存到硬盘
//                .into(simpleTarget);//使用此方法，需要再调用asBitmap(),因为加载的资源可能是gif
                .into(imageView);//
        LinearLayout ll = (LinearLayout) baseViewHolder.getConvertView().findViewById(R.id.ll);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_title);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
    }
}
