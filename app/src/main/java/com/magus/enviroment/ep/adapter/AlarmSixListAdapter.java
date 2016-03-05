package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmSixListInfo;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class AlarmSixListAdapter extends BaseAdapter {

    private List<AlarmSixListInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AlarmSixListAdapter(Context context, List<AlarmSixListInfo> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AlarmSixListInfo> list) {
//        if (list != null) {
        this.mList = list;
        notifyDataSetChanged();
//        } else {
//            return;
//        }
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
            convertView = mLayoutInflater.inflate(R.layout.item_alarm_six_list_adapter, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_detail_layout);
            holder.pollSourceName = (TextView) convertView.findViewById(R.id.attention_detial_set);
            holder.alarmTypeName = (TextView) convertView.findViewById(R.id.attention_detail_type);
            holder.factoryName = (TextView) convertView.findViewById(R.id.attention_detial_facility);
            holder.detailTime = (TextView) convertView.findViewById(R.id.attention_detail_description);
            holder.breakTime = (TextView) convertView.findViewById(R.id.attention_detail_break_time);
            holder.chroma = (TextView) convertView.findViewById(R.id.attention_detail_chroma);
            holder.times = (TextView) convertView.findViewById(R.id.attention_detail_times);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AlarmSixListInfo detailInfo = mList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }
        holder.pollSourceName.setText(detailInfo.getPoll_source_name());
        holder.alarmTypeName.setText(detailInfo.getAlarm_type_name());
        holder.factoryName.setText(detailInfo.getFacility_name()+"（"+detailInfo.getAlarm_detal()+"）");
        holder.detailTime.setText(detailInfo.getAlarmtime());
        if (!"0".equals(detailInfo.getTimecou())){
            holder.breakTime.setText("时　　长:"+detailInfo.getTimecou()+"分");
        }
        if (!"".equals(detailInfo.getOver_chroma())){
            holder.chroma.setText("超标浓度:"+detailInfo.getOver_chroma());
    }
        if (!"".equals(detailInfo.getOver_times())){
            holder.times.setText("超标倍数:"+detailInfo.getOver_times());
        }


        return convertView;
    }


    class ViewHolder {
        LinearLayout layout;
        TextView pollSourceName;//电厂名称
        TextView factoryName;//机组名称
        TextView detailTime;//发生时间
        TextView alarmTypeName;//异常类型名称
        TextView breakTime;//数据中断时长
        TextView chroma;//超标浓度
        TextView times;//超标倍数
    }
}
