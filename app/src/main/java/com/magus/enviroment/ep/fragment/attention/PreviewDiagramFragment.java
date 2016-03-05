package com.magus.enviroment.ep.fragment.attention;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.adapter.AttentionAbnormalAdapter;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.AlarmGridPercentage;
import com.magus.enviroment.ep.bean.EnterpriseRateInfo;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.magusutils.ContextUtil;
import com.magus.magusutils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常总览上部分表g格
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention
 * 2015-15/5/13-上午9:44.
 */
public class PreviewDiagramFragment extends BaseFragment {

    private View mRootView;

    private ListView mListView;
    private AttentionAbnormalAdapter mAdapter;

    private List<AlarmGridPercentage> mRateList= new ArrayList<AlarmGridPercentage>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_attention_preview_diagram, null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initData(){
        mRateList = (List<AlarmGridPercentage>) SharedPreferenceUtil.getObject(SpKeyConstant.ENTERPRISE_RATE_LIST, ContextUtil.getContext());
    }
    private void initView(){
        mListView = (ListView) mRootView.findViewById(R.id.abnormal_list);
        mAdapter = new AttentionAbnormalAdapter(getActivity(),mRateList);
        mListView.setAdapter(mAdapter);
    }
}
