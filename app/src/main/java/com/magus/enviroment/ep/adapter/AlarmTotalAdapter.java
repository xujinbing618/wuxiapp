package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmTotal;

import java.util.List;

/**
 * Created by Administrator on 2015/10/23.
 */
public class AlarmTotalAdapter extends BaseAdapter {
    private static final String TAG = "AlarmTotalAdapter";
    private List<AlarmTotal> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AlarmTotalAdapter(Context context, List<AlarmTotal> list) {

        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AlarmTotal> list) {
        if (list != null) {
            this.mList = list;
            notifyDataSetChanged();
        } else {
            return;
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
            convertView = mLayoutInflater.inflate(R.layout.item_alarm_total_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_alarm_total_layout);
            holder.pollSourceName = (TextView) convertView.findViewById(R.id.attention_alarm_total_layout_pollSourceName);
            holder.alarmTime = (TextView) convertView.findViewById(R.id.attention_alarm_total_layout_alarmTime);
            holder.alarmSoutce = (TextView) convertView.findViewById(R.id.attention_alarm_total_layout_source);
            holder.alarmValue = (TextView) convertView.findViewById(R.id.attention_alarm_total_layout_value);
            holder.alarmLetValue = (TextView) convertView.findViewById(R.id.attention_alarm_total_layout_let_value);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AlarmTotal detailInfo = mList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }
        holder.pollSourceName.setText(detailInfo.getPoll_source_name());
        holder.alarmTime.setText(detailInfo.getBegin_time());
        holder.alarmSoutce.setText(detailInfo.getType());
        holder.alarmValue.setText(detailInfo.getWarning());
        holder.alarmLetValue.setText(detailInfo.getNum());
        return convertView;
    }

    class ViewHolder {
        LinearLayout layout;
        //企业名称
        TextView pollSourceName;
        //发生时间
        TextView alarmTime;
        //污染源
        TextView alarmSoutce;
        //预警值
        TextView alarmValue;

        TextView alarmLetValue;//排放值
    }
}
