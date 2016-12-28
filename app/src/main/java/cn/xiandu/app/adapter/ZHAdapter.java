package cn.xiandu.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.TopicDetali;
import cn.xiandu.app.utils.ImageUtils;

/**
 * Created by dell on 2016/12/26.
 */

public class ZHAdapter extends BaseQuickAdapter<TopicDetali> {

    private Context context;
    public ZHAdapter(Context context ,int layoutResId, List<TopicDetali> data) {
        super(layoutResId, data);
        this.context = context ;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TopicDetali topicDetali) {
        baseViewHolder.setText(R.id.tv_title,topicDetali.getTitle());
        baseViewHolder.setText(R.id.tv_desc,topicDetali.getDesc());
        baseViewHolder.setText(R.id.tv_num,topicDetali.getAgreeNum() + "赞同");
        baseViewHolder.setText(R.id.tv_author,topicDetali.getAuthor());
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.iv_image);
        if (TextUtils.isEmpty(topicDetali.getImageUrl())){
            imageView.setVisibility(View.GONE);
        }else{
            imageView.setVisibility(View.VISIBLE);
            ImageUtils.loadImage(context,topicDetali.getImageUrl(),imageView);
        }
    }
}
