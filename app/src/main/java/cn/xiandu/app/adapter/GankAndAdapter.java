package cn.xiandu.app.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.GankAnd;
import cn.xiandu.app.utils.ImageUtils;

/**
 * Created by dell on 2016/12/2.
 */

public class GankAndAdapter extends BaseQuickAdapter<GankAnd> {

    private Context context ;
    public GankAndAdapter(Context context ,int layoutResId, List<GankAnd> data) {
        super(layoutResId, data);
        this.context = context ;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GankAnd gankAnd) {
        baseViewHolder.setText(R.id.tv_title, gankAnd.getDesc());
        baseViewHolder.setText(R.id.tv_time,gankAnd.getCreatedAt().split("T")[0]);
        baseViewHolder.setVisible(R.id.tv_desc,false);
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image);
        if (gankAnd.getImages() != null && gankAnd.getImages().size() > 0){
            ImageUtils.loadImage(context,gankAnd.getImages().get(0),imageView);
        }

    }
}
