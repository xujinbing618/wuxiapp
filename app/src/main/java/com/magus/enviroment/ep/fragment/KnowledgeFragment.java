package com.magus.enviroment.ep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.activity.knowledge.CaseActivity;
import com.magus.enviroment.ep.activity.knowledge.KnowledgeActivity;
import com.magus.enviroment.ep.activity.knowledge.PolicyFileActivity;
import com.magus.enviroment.ep.activity.knowledge.WorkIntroActivity;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ui.CustomActionBar;


/**
 * 环保知识
 * Created by pau on 3/15/15.
 */
public class KnowledgeFragment extends BaseFragment {
    private static final String TAG = "KnowledgeFragment";
    private CustomActionBar mActionBar;
    private View mRootView;
    private ImageView btnPolicyFile;
    private ImageView btnKnowledge;
    private ImageView btnWork;
    private ImageView btnCase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_knowledge, null);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initActionBar();
        initView();
    }

    private void initView() {
        btnPolicyFile = (ImageView) mRootView.findViewById(R.id.knowledge_policy_file);
        btnPolicyFile.setOnClickListener(onClickListener);
        btnCase = (ImageView) mRootView.findViewById(R.id.knowledge_case);
        btnCase.setOnClickListener(onClickListener);
        btnKnowledge = (ImageView) mRootView.findViewById(R.id.knowledge_knowledge);
        btnKnowledge.setOnClickListener(onClickListener);
        btnWork = (ImageView) mRootView.findViewById(R.id.knowledge_work);
        btnWork.setOnClickListener(onClickListener);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) mRootView.findViewById(R.id.custom_action_bar);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.knowledge_policy_file:
                    startNewActivity(PolicyFileActivity.class);

                    break;
                case R.id.knowledge_knowledge:
                    startNewActivity(KnowledgeActivity.class);

                    break;
                case R.id.knowledge_case:
                    startNewActivity(CaseActivity.class);

                    break;
                case R.id.knowledge_work:
                    startNewActivity(WorkIntroActivity.class);

                    break;
            }
        }
    };

    private void startNewActivity(Class<?> cls) {

        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
