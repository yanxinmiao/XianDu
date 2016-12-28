package cn.xiandu.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.TopicDetali;

/**
 * Created by dell on 2016/12/26.
 */

public class ZlistAdapter extends BaseQuickAdapter<TopicDetali>{

    public ZlistAdapter(int layoutResId, List<TopicDetali> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TopicDetali topicDetali) {
        baseViewHolder.setText(R.id.tv_desc,topicDetali.getDesc());
        baseViewHolder.setText(R.id.tv_num,topicDetali.getAgreeNum() + "赞同");
        baseViewHolder.setText(R.id.tv_author,topicDetali.getAuthor());
    }
}
