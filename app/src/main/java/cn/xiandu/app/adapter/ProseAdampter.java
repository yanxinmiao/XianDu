package cn.xiandu.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.ProseBean;
import cn.xiandu.app.utils.ImageUtils;
import cn.xiandu.app.utils.ThemeManager;

/**
 * Created by dell on 2016/12/15.
 */

public class ProseAdampter extends BaseQuickAdapter<ProseBean> {

    private Context context ;
    public ProseAdampter(Context context ,int layoutResId, List<ProseBean> data) {
        super(layoutResId, data);
        this.context = context ;
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, ProseBean proseBean) {
        baseViewHolder.setText(R.id.tv_title,proseBean.getTitle());
        baseViewHolder.setText(R.id.tv_author,proseBean.getAuthor());
        baseViewHolder.setText(R.id.tv_desc,proseBean.getDesc());
        String time = proseBean.getTime();
        if (time.length() > 3){
            baseViewHolder.setText(R.id.tv_time,proseBean.getTime().substring(3,proseBean.getTime().length()));
        }else{
            baseViewHolder.setText(R.id.tv_time,proseBean.getTime());
        }
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image);
        if (TextUtils.isEmpty(proseBean.getImageUrl())){
            baseViewHolder.setVisible(R.id.image,false);
        }else{
            baseViewHolder.setVisible(R.id.image,true);
            ImageUtils.loadImage(context,"http://www.lookmw.cn" + proseBean.getImageUrl(),imageView);
        }
        RelativeLayout ll = (RelativeLayout) baseViewHolder.getConvertView().findViewById(R.id.rl_prose);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_title);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
        TextView tvDesc = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_desc);
        tvDesc.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_666666)));
        TextView tvAuthor = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_author);
        tvAuthor.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_999999)));
        TextView tvTime = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_time);
        tvTime.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_999999)));
    }
}
