package cn.xiandu.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.NovelBean;
import cn.xiandu.app.utils.ImageUtils;
import cn.xiandu.app.utils.ThemeManager;
import me.codeboy.android.aligntextview.AlignTextView;

/**
 * Created by dell on 2016/12/15.
 */

public class NovelAdapter extends BaseQuickAdapter<NovelBean> {

    private Context context;
    public NovelAdapter(Context context ,int layoutResId, List<NovelBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NovelBean novelBean) {
        baseViewHolder.setText(R.id.tv_noveltitle,novelBean.getTitle());
        if (baseViewHolder.getAdapterPosition() == 0){
            Logger.d("novelBean :" + novelBean.getTitle());
            Logger.d("novelBean :" + novelBean.getDesc());
        }
        AlignTextView tvDesc = (AlignTextView) baseViewHolder.getConvertView().findViewById(R.id.tv_novel_desc);
        tvDesc.setText(null);
        tvDesc.setText(novelBean.getDesc());
//        baseViewHolder.setText(R.id.tv_novel_desc,novelBean.getDesc());
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image_novel);
        if (TextUtils.isEmpty(novelBean.getImageUrl()) || "/images/defaultpic.gif".equals(novelBean.getImageUrl())){
            baseViewHolder.setVisible(R.id.image_novel,false);
        }else{
            baseViewHolder.setVisible(R.id.image_novel,true);
            ImageUtils.loadImage(context,"http://www.aikann.com" + novelBean.getImageUrl(),imageView);
        }

        RelativeLayout ll = (RelativeLayout) baseViewHolder.getConvertView().findViewById(R.id.rl_novel);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_noveltitle);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
        tvDesc.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
    }
}
