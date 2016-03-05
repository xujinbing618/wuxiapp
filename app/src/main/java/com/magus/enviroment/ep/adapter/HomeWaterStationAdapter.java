package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magus.enviroment.R;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class HomeWaterStationAdapter extends BaseAdapter {
    private static final String TAG = "HomeWaterStationAdapter";

    private List<String> mList;
    private LayoutInflater mLayoutInflater ;
    private Context mContext;

    public HomeWaterStationAdapter(Context context,List<String> list){
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<String> list){
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();//初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_air_staion_layout, null);
            holder.number = (TextView) convertView.findViewById(R.id.air_num);
            holder.station = (TextView) convertView.findViewById(R.id.air_station);
            holder.range = (TextView) convertView.findViewById(R.id.air_aqi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }




    class ViewHolder{
        TextView number;
        TextView station;
        TextView range;
    }
}
