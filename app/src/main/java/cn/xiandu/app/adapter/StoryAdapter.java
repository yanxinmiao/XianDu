package cn.xiandu.app.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.StoryBean;
import cn.xiandu.app.utils.ThemeManager;

/**
 * Created by dell on 2016/12/15.
 */

public class StoryAdapter extends BaseQuickAdapter<StoryBean> {

    private Context context;
    public StoryAdapter(Context context ,int layoutResId, List<StoryBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, StoryBean storyBean) {
        baseViewHolder.setText(R.id.tv_title,storyBean.getTitle());
        baseViewHolder.setText(R.id.tv_time,storyBean.getTime().substring(3 ,storyBean.getTime().length()));

        RelativeLayout ll = (RelativeLayout) baseViewHolder.getConvertView().findViewById(R.id.rl_story);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_title);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
        TextView tvTime = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_time);
        tvTime.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_999999)));
    }
}
