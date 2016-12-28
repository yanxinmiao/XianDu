package cn.xiandu.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.bean.NaowanBean;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class NaoWanFragment extends BaseFragment {

    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_quest)
    TextView tvQuest;
    private View view;
    private int MAX_SIZE = 10;
    private Gson gson;
    private int pageCode = 1;
    private List<NaowanBean> list = new ArrayList<>();
    private String title;

    public NaoWanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
            ButterKnife.bind(this, view);
            gson = new Gson();
            getData();
        }
        return view;
    }
    private void getData() {
        OkHttpUtils
                .get()
                .url(Constant.NAOWAN_URL)
                .addHeader("apikey", "b1d04a07a5a857be7dbea2ac738ec6e4")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("", "e:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.json(response);
//                        if (swipeLayout.isRefreshing()) {
//                            swipeLayout.setRefreshing(false);
//                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                JSONArray ja = jsonObject.optJSONArray("newslist");
//                                if (ja.length() < MAX_SIZE) {
//                                    mAdapter.loadComplete();
//                                }
                                Type t = new TypeToken<List<NaowanBean>>() {
                                }.getType();
                                List<NaowanBean> ls = gson.fromJson(ja.toString(), t);
                                if (ls != null && ls.size() > 0){
                                    tvQuest.setText(ls.get(0).getQuest());
                                    list.addAll(ls);
                                }
//                                mAdapter.addData(ls);
//                                pageCode++;
                            } else {
                                ToastUtils.showMyToast(jsonObject.optString("errMsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    @OnClick({R.id.tv_next, R.id.tv_result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                pageCode = 1;
                list.clear();
                getData();
                break;
            case R.id.tv_result:
                new AlertDialog.Builder(getActivity()).setTitle("答案").setMessage(list.get(0).getResult()).setPositiveButton("知道了", null).show();
                break;
        }
    }

}
