package cn.xiandu.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.NaowanBean;

/**
 * Created by dell on 2016/12/1.
 */

public class NaowanAdapter extends BaseQuickAdapter<NaowanBean> {
    public NaowanAdapter(int layoutResId, List<NaowanBean> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, NaowanBean naowanBean) {
        baseViewHolder.setText(R.id.tv_quest,naowanBean.getQuest());

    }
}
