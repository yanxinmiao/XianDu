package cn.xiandu.app.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.JokeTextBean;
import cn.xiandu.app.utils.ThemeManager;

/**
 * Created by dell on 2016/12/1.
 */

public class JokeTextAdapter extends BaseQuickAdapter<JokeTextBean> {

    private Context context ;
    public JokeTextAdapter(Context context, int layoutResId, List<JokeTextBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, JokeTextBean jokeTextBean) {
        baseViewHolder.setText(R.id.tv_title,jokeTextBean.getTitle());
        baseViewHolder.setText(R.id.tv_content, Html.fromHtml(jokeTextBean.getText()));
        LinearLayout ll = (LinearLayout) baseViewHolder.getConvertView().findViewById(R.id.ll);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_title);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));

        TextView tvContent = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_content);
        tvContent.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.color_666666)));

    }
}
