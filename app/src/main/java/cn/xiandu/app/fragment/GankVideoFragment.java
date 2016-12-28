package cn.xiandu.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xiandu.app.activity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankVideoFragment extends BaseFragment {


    public GankVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gank_video, container, false);
    }

}
