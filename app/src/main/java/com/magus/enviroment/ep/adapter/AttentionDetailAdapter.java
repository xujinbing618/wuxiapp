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
import com.magus.enviroment.ep.constant.CodeConstant;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class AttentionDetailAdapter extends BaseAdapter {
    private static final String TAG = "AttentionDetailAdapter";

    private List<AlarmDetailInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionDetailAdapter(Context context, List<AlarmDetailInfo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_attention_detail_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_detail_layout);
            holder.pollSourceName = (TextView) convertView.findViewById(R.id.attention_detial_set);
            holder.alarmTypeName = (TextView) convertView.findViewById(R.id.attention_detail_type);
            holder.description = (TextView) convertView.findViewById(R.id.attention_detail_description);
            holder.dealStatus = (TextView) convertView.findViewById(R.id.status);
            holder.facilityName = (TextView) convertView.findViewById(R.id.attention_detial_facility);
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
        holder.pollSourceName.setText(detailInfo.getPollSourceName());
        holder.facilityName.setText(detailInfo.getFacilityName());
        holder.alarmTypeName.setText(detailInfo.getAlarmTypeName());
        String dealStatus = detailInfo.getDealStatus();
        String dealText = "";
        if (CodeConstant.STATUS_PLAN_HANDLE.equals(dealStatus)) {
            dealText = "待处理";
        } else if (CodeConstant.STATUS_OVERDUE_UNHANDLED.equals(dealStatus)) {
            dealText = "逾期未处理";
        } else if (CodeConstant.STATUS_HANDLED.equals(dealStatus)) {
            dealText = "已处理";
        } else if (CodeConstant.STATUS_THIRD.equals(dealStatus)) {
            dealText = "第三方处理";
        } else if (CodeConstant.STATUS_OVERDUE_PLAN_HANDLE.equals(dealStatus)) {
            dealText = "延期处理";
        } else if (CodeConstant.STATUS_OVERDUE_HANDLED.equals(dealStatus)) {
            dealText = "延期已处理";
        }
        holder.dealStatus.setText(dealText);
        holder.description.setText(detailInfo.getAlarmTime() + ",发生" + detailInfo.getAlarmTypeName() + ",共" + detailInfo.getNum() + "次,总时长" + detailInfo.getTimeCou() + "分钟");
        return convertView;
    }


    class ViewHolder {
        LinearLayout layout;
        TextView pollSourceName;
        TextView description;
        TextView alarmTypeName;
        TextView dealStatus;
        TextView facilityName;
    }
}
