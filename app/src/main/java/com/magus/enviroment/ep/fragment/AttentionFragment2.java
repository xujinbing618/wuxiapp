//package com.magus.enviroment.ep.fragment;
//
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//
//import com.magus.enviroment.R;
//import com.magus.enviroment.ep.MyApplication;
//import com.magus.enviroment.ep.base.BaseFragment;
//import com.magus.enviroment.ep.fragment.attention.DetailFragment;
//import com.magus.enviroment.ep.fragment.attention.ReportFragment;
//import com.magus.enviroment.ep.fragment.attention.PreviewFragment;
//
//import java.util.ArrayList;
//
///**
// * 关注页面（带viewpager）
// * Created by pau
// * Packagename com.magus.enviroment.ep.fragment
// * 2015-15/5/10-上午9:29.
// */
//public class AttentionFragment2 extends BaseFragment {
//    private static final String TAG="AttentionFragment2";
//    private View mRootView;
//
//    private static int ID_TOTAL = 0;//异常总览
//    private static int ID_DETAIL = 1;//异常详情
//    private static int ID_REPORT = 2;//报告
//
//    private FragmentManager mFragmentManager;
//    private ArrayList<Fragment> mViewPagerChildList = new ArrayList<Fragment>();
//    private PreviewFragment mTotalFragment;
//    private DetailFragment mDetailFragment;
//    private ReportFragment mReportFragment;
//
//    private ViewPager mNestedViewPager;
//    private MyViewPagerAdapter mPagerAdapter;
//
//
//    private ImageView detailSelectButton;//详情选择按钮
//    private RelativeLayout[] mTopButtons;
//    private int[] mTopButtonIds = {R.id.attention_total, R.id.attention_detail, R.id.attention_report};
//
//    private PopupWindow mPopupWindow;//详情弹窗按钮
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mRootView == null) {
//            mRootView = inflater.inflate(R.layout.fragment_attention2, container, false);
//            initView();
//        }
//        ViewGroup parent = (ViewGroup) mRootView.getParent();
//        if (parent != null) {
//            parent.removeView(mRootView);
//        }
//        return mRootView;
//    }
//
//
//    private void initView() {
//        mNestedViewPager = (ViewPager) mRootView.findViewById(R.id.nestedViewPager);
//        mViewPagerChildList.clear();
//        //初始化fragment
//        if (mTotalFragment == null) {
//            mTotalFragment = new PreviewFragment();
//        }
//        mViewPagerChildList.add(mTotalFragment);
//
//        if (mDetailFragment == null) {
//            mDetailFragment = new DetailFragment();
//        }
//        mViewPagerChildList.add(mDetailFragment);
//
//        if (mReportFragment == null) {
//            mReportFragment = new ReportFragment();
//        }
//        mViewPagerChildList.add(mReportFragment);
//
//        // 设置ViewPager的adapter
//        mPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
//        mNestedViewPager.setAdapter(mPagerAdapter);
//        //设置ViewPager改变监听
//        mNestedViewPager.setOnPageChangeListener(mPageChangeListener);
//
//        mFragmentManager = getChildFragmentManager();
//        detailSelectButton = (ImageView) mRootView.findViewById(R.id.attention_detail_select);
//        detailSelectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initPopWindow();
//            }
//        });
//
//        mTopButtons = new RelativeLayout[mTopButtonIds.length];
//        for (int i = 0; i < mTopButtonIds.length; i++) {
//            mTopButtons[i] = (RelativeLayout) mRootView.findViewById(mTopButtonIds[i]);
//            mTopButtons[i].setOnClickListener(onClickListener);
//        }
//        mTopButtons[0].setSelected(true);
//    }
//
//    //viewpager改变监听
//    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            switch (position) {
//                case 0:
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    break;
//            }
//            setMenuChecked(position);
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };
//
//    //初始化popupWindow
//    private void initPopWindow() {
//        //showAsDropDown(View anchor)：相对某个控件的位置（正左下方），无偏移
//        //showAsDropDown(View anchor, int xoff, int yoff)：相对某个控件的位置，有偏移
//        //showAtLocation(View parent, int gravity, int x, int y)：相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
//        View popView = mLayoutInflater.inflate(R.layout.attention_popup_window_layout, null);
//        mPopupWindow = new PopupWindow(popView, MyApplication.mWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
//        // 需要设置一下此参数，点击外边可消失
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        //设置点击窗口外边窗口消失
//        mPopupWindow.setOutsideTouchable(true);
//        // 设置此参数获得焦点，否则无法点击
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.showAsDropDown(mTopButtons[ID_DETAIL]);
//
//    }
//
//
//    //菜单点击事件
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.attention_total:
//                    setMenuChecked(ID_TOTAL);
//                    break;
//
//                case R.id.attention_detail:
//                    setMenuChecked(ID_DETAIL);
//                    break;
//
//                case R.id.attention_report:
//                    setMenuChecked(ID_REPORT);
//                    break;
//            }
//        }
//    };
//
//    private void setMenuChecked(int checkedId){
//        mNestedViewPager.setCurrentItem(checkedId);
//        for (int i=0;i<3;i++){
//            mTopButtons[i].setSelected(false);
//        }
//        mTopButtons[checkedId].setSelected(true);
//    }
//
//    //viewpager适配器
//    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        public MyViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int arg0) {
//            return mViewPagerChildList.get(arg0);
//        }
//
//        @Override
//        public int getCount() {
//            return mViewPagerChildList.size();
//        }
//
//    }
//}
