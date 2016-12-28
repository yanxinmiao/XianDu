package cn.xiandu.app.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.GankPicData;
import cn.xiandu.app.utils.CommonTool;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by dell on 2016/12/1.
 */

public class PicAdapter extends BaseQuickAdapter<GankPicData> {

    private Context context ;
    public PicAdapter(Context context ,int layoutResId, List<GankPicData> data) {
        super(layoutResId, data);
        this.context = context ;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GankPicData picBean) {
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image);

        if (mHeights != null && mHeights.size() > 0){
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            int position = baseViewHolder.getAdapterPosition();
            if (mHeights.size() > position){
                lp.height = mHeights.get(position);
            }
            imageView.setLayoutParams(lp);
        }
        Glide
                .with(context) // could be an issue!
                .load(picBean.getUrl())
                .bitmapTransform(new CenterCrop(context),new ColorFilterTransformation(context,R.color.colorAccent),new RoundedCornersTransformation(context,CommonTool.dip2px(5),0))
                .crossFade()
                .placeholder(R.drawable.default_news_cat_pic)
                .error(R.drawable.default_news_cat_pic)
                .into(imageView);
    }
    private List<Integer> mHeights;
    public void setHeights(List<GankPicData> list){
        int max = 220;
        int min = 170;
        Random random = new Random();
        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            int s = random.nextInt(max) % (max - min + 1) + min;//生成指定区间内的随机数
            mHeights.add(CommonTool.dip2px(s));
        }
    }
}
