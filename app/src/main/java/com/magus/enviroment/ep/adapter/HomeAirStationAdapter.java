package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AirInfoBean;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class HomeAirStationAdapter extends BaseAdapter {
    private static final String TAG = "HomeAirStationAdapter";

    private List<AirInfoBean> mList;
    private LayoutInflater mLayoutInflater ;
    private Context mContext;

    public HomeAirStationAdapter(Context context,List<AirInfoBean> list){
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AirInfoBean> list){
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
            holder.aqi = (TextView) convertView.findViewById(R.id.air_aqi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.number.setText(position+1+"");
        holder.station.setText(mList.get(position).getPosition_name().trim());
        holder.aqi.setText(mList.get(position).getAqi().trim());
        int aqiIndex = Integer.parseInt(mList.get(position).getAqi().trim());
        if (aqiIndex>0&&aqiIndex<=50){
            holder.aqi.setBackgroundResource(R.drawable.air_range_sq_bg_1);

        }else if (aqiIndex>50&&aqiIndex<=100){
            holder.aqi.setBackgroundResource(R.drawable.air_range_sq_bg_2);

        }else if (aqiIndex>100&&aqiIndex<=150){
            holder.aqi.setBackgroundResource(R.drawable.air_range_sq_bg_3);

        }else if (aqiIndex>150&&aqiIndex<=200){
            holder.aqi.setBackgroundResource(R.drawable.air_range_sq_bg_4);

        }else if (aqiIndex>200&&aqiIndex<=300){
            holder.aqi.setBackgroundResource(R.drawable.air_range_sq_bg_5);

        }else {
            holder.aqi.setBackgroundResource(R.drawable.air_range_sq_bg_6);
        }

        return convertView;
    }




    class ViewHolder{
        TextView number;
        TextView station;
        TextView aqi;
    }
}
