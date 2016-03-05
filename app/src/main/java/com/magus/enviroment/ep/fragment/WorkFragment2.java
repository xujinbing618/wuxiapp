package com.magus.enviroment.ep.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.fragment.work.WorkCheckedFragment;
import com.magus.enviroment.ep.fragment.work.WorkHandleFragment;
import com.magus.enviroment.ep.fragment.work.WorkUnCheckedFragment;

import java.util.ArrayList;


/**
 * 工作界面（带viewpager）
 * Created by pau on 3/15/15.
 */
public class WorkFragment2 extends BaseFragment {
    private static final String TAG  = "TopicFragment";

    private View mRootView;

    private ArrayList<Fragment> mViewPagerChildList = new ArrayList<Fragment>();
    private WorkUnCheckedFragment mUnCheckedFragment;
    private WorkHandleFragment mHandleFragment;
    private WorkCheckedFragment mCheckedFragment;

    private TextView mUncheckedText;
    private TextView mHandleText;
    private TextView mCheckedText;

    private ViewPager mNestedViewPager;
    private MyViewPagerAdapter mPagerAdapter;
    private ImageView mActiveIndicator;

    // 最后一次选中的fragment index
    private int mLastItemIndex = -1;

    //用于获取屏幕宽高
    private WindowManager wm ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_work2, container, false);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null){
            parent.removeView(mRootView);
        }
        return mRootView ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(){
        wm = getActivity().getWindowManager();

        mNestedViewPager = (ViewPager) mRootView.findViewById(R.id.nestedViewPager);
        mViewPagerChildList.clear();

        //初始化fragment
        //待处理
        if (mUnCheckedFragment==null){
            mUnCheckedFragment=new WorkUnCheckedFragment();
        }
        mViewPagerChildList.add(mUnCheckedFragment);

        //已处理
        if (mHandleFragment==null){
            mHandleFragment=new WorkHandleFragment();
        }
        mViewPagerChildList.add(mHandleFragment);

        //已检查
        if (mCheckedFragment==null){
            mCheckedFragment=new WorkCheckedFragment();
        }
        mViewPagerChildList.add(mCheckedFragment);

        // 设置ViewPager的adapter
        mPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        mNestedViewPager.setOnPageChangeListener(mPageChangeListener);
        mNestedViewPager.setAdapter(mPagerAdapter);

        //viewpager标题
        mUncheckedText = (TextView) mRootView.findViewById(R.id.unchecked);
        mUncheckedText.setOnClickListener(mListener);
        mHandleText = (TextView) mRootView.findViewById(R.id.handle);
        mHandleText.setOnClickListener(mListener);
        mCheckedText = (TextView) mRootView.findViewById(R.id.is_checked);
        mCheckedText.setOnClickListener(mListener);

        //viewpager指示器
        mActiveIndicator = (ImageView) mRootView.findViewById(R.id.indicator);
        //设置指示器参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wm.getDefaultDisplay().getWidth()/3,10);
        mActiveIndicator.setLayoutParams(params);
    }

    //viewpager改变监听
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int width = mActiveIndicator.getWidth();
            mActiveIndicator.setTranslationX(width * position + positionOffset * width);
        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:

                    break;

                case 1:

                    break;
                case 2:

                    break;

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.unchecked:
                    mNestedViewPager.setCurrentItem(0);
                    break;

                case R.id.handle:

                    mNestedViewPager.setCurrentItem(1);
                    break;

                case R.id.is_checked:

                    mNestedViewPager.setCurrentItem(2);
                    break;

            }
        }
    };

    //viewpager适配器
    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mViewPagerChildList.get(arg0);
        }

        @Override
        public int getCount() {
            return mViewPagerChildList.size();
        }

    }
}
