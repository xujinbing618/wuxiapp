package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmDetailInfo;

import java.util.List;

/**
 * 超标预警
 * Created by Xuer on 2015/10/14.
 */
public class AttentionAlarmOverAdapter extends BaseAdapter {
    private static final String TAG = "AttentionAlarmOverAdapter";
    private List<AlarmDetailInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionAlarmOverAdapter(Context context, List<AlarmDetailInfo> list) {

        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AlarmDetailInfo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_alarm_over_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_alarm_over_layout);
            holder.pollSourceName = (TextView) convertView.findViewById(R.id.attention_alarm_over_layout_pollSourceName);
            holder.alarmTime = (TextView) convertView.findViewById(R.id.attention_alarm_over_layout_alarmTime);
            holder.alarmTypeName=(TextView)convertView.findViewById(R.id.attention_alarm_over_layout_source);
            holder.alarmLetValue=(TextView)convertView.findViewById(R.id.attention_alarm_over_layout_value);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AlarmDetailInfo detailInfo = mList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }
        holder.pollSourceName.setText(detailInfo.getPollSourceName()+"("+detailInfo.getFacility_name()+")");
        holder.alarmTime.setText(detailInfo.getBegin_time());
        holder.alarmTypeName.setText(detailInfo.getPollutantName());
        holder.alarmLetValue.setText(detailInfo.getFact_out_nd());
        return convertView;
    }

    class ViewHolder {
        LinearLayout layout;
        TextView pollSourceName;
        TextView alarmTime;
        TextView alarmTypeName;
        TextView alarmLetValue;//排放值

    }
}
