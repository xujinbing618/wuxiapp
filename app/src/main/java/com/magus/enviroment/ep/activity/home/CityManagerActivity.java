package com.magus.enviroment.ep.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.adapter.DefaultCityAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.CityInfoBean;
import com.magus.enviroment.ep.constant.CityConstant;
import com.magus.enviroment.ep.dao.CityManagerDao;
import com.magus.enviroment.ui.CustomActionBar;

import java.util.ArrayList;

/**
 * Created by pau
 * Packagename com.magus.enviroment.activity
 * 2015-15/4/16-下午2:05.
 */
public class CityManagerActivity extends SwipeBaseActivity {

    private CustomActionBar mActionBar;
    private DefaultCityAdapter mAdapter;
    private ListView mCityListView;
    private ArrayList<CityInfoBean> mList = new ArrayList<CityInfoBean>();
    private CityManagerDao mManagerDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        initActionBar();
        initView();
        initDate();
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(CityManagerActivity.this);
    }
    private void initView() {
        mCityListView = (ListView) findViewById(R.id.city_list);
        mAdapter = new DefaultCityAdapter(this, getList());
        mCityListView.setAdapter(mAdapter);
        setListener();
    }

    private void initDate() {
        mManagerDao = CityManagerDao.getInstance(this);
    }

    private ArrayList<CityInfoBean> getList() {
        for (int i=0;i< CityConstant.cityArray.length;i++){
            CityInfoBean infoBean = new CityInfoBean();
            infoBean.setCity(CityConstant.cityArray[i]);
            infoBean.setCityPinYin(CityConstant.cityPinYin[i]);
            infoBean.setProvince("内蒙古");
            mList.add(infoBean);
        }

        return mList;
    }

    private void setListener() {
        mCityListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        mManagerDao.insertCity(mList.get(position));
                    }
                });

    }
}
