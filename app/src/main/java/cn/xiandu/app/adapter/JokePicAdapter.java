package cn.xiandu.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.PicViewActivity;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.JokePicBean;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.ImageUtils;
import cn.xiandu.app.utils.ThemeManager;

/**
 * Created by dell on 2016/12/8.
 */

public class JokePicAdapter extends BaseQuickAdapter<JokePicBean> {
    private Context context ;
    public JokePicAdapter(Context context ,int layoutResId, List<JokePicBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final JokePicBean jokePicBean) {
        baseViewHolder.setVisible(R.id.tv_desc, false);
        baseViewHolder.setText(R.id.tv_title,jokePicBean.getTitle());
        baseViewHolder.setText(R.id.tv_time,jokePicBean.getCt().split(" ")[0]);
        ImageView imageView = (ImageView) baseViewHolder.getConvertView().findViewById(R.id.image);
        ImageUtils.loadImage(context,jokePicBean.getImg(),imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicViewActivity.class);
                intent.putExtra("url",jokePicBean.getImg());
                intent.putExtra("id", CommonTool.getMD5Str(jokePicBean.getImg()));
                context.startActivity(intent);
            }
        });

        LinearLayout ll = (LinearLayout) baseViewHolder.getConvertView().findViewById(R.id.ll);
        ll.setBackgroundColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.backgroundColor)));
        TextView tvTitle = (TextView) baseViewHolder.getConvertView().findViewById(R.id.tv_title);
        tvTitle.setTextColor(context.getResources().getColor(ThemeManager.getCurrentThemeRes(context, R.color.textColor)));
    }
}
