package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.CityInfoBean;
import com.magus.enviroment.ep.dao.CityManagerDao;

import java.util.HashSet;
import java.util.List;

/**
 * Created by pau on 1/8/15.
 */
public class DefaultCityAdapter extends BaseAdapter {

    private Context mContext;
    private List<CityInfoBean> mList;
    private LayoutInflater mInflater;
    private CityManagerDao mManagerDao;
    private HashSet<String> cityNames;
    private Resources mResources;
    private boolean isCityAdded = false;


    public DefaultCityAdapter(Context context, List<CityInfoBean> list) {
        this.mContext = context;
        this.mList = list;
        this.mManagerDao = new CityManagerDao(mContext);
        this.mResources = mContext.getResources();
        cityNames = mManagerDao.queryCityNames();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setList(List<CityInfoBean> list){
        if (list!=null){
            this.mList = list;
            notifyDataSetChanged();
        }else {
            return ;
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView==null){
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_default_city_list,null);
            holder.cityText = (TextView) convertView.findViewById(R.id.city);
            holder.addImage = (ImageView) convertView.findViewById(R.id.add_city);
            holder.addLayout = (RelativeLayout) convertView.findViewById(R.id.add_layout);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        final CityInfoBean infoBean = mList.get(position);
        isCityAdded = cityNames.contains(mList.get(position).getCity());
        holder.cityText.setText(infoBean.getCity());
        if (isCityAdded){
            holder.addImage.setImageDrawable(mResources.getDrawable(R.mipmap.added_city));
        }else {
            holder.addImage.setImageDrawable(mResources.getDrawable(R.mipmap.add_city));
        }
        holder.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isCityAdded){
                holder.addImage.setImageDrawable(mResources.getDrawable(R.mipmap.add_city));
                isCityAdded = !isCityAdded;
                mManagerDao.deleteCity(infoBean);
            }else {
                holder.addImage.setImageDrawable(mResources.getDrawable(R.mipmap.added_city));
                isCityAdded = !isCityAdded;
                mManagerDao.insertCity(infoBean);
            }
            }
        });

        return convertView;
    }

    class Holder{
        TextView cityText;
        ImageView addImage;
        RelativeLayout addLayout;
    }
}
