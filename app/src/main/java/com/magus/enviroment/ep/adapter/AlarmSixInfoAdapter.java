package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmSixMessageInfo;
import com.magus.enviroment.ep.constant.CodeConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/11/25.
 */
public class AlarmSixInfoAdapter extends BaseAdapter {
    private List<AlarmSixMessageInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    List<String> sId = new ArrayList<>();//选中的id列表

    public AlarmSixInfoAdapter(Context context, List<AlarmSixMessageInfo> list) {
        isSelected = new HashMap<Integer, Boolean>();
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }
    // 初始化isSelected的数据
    private void initDate(List<AlarmSixMessageInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }
    public void setList(List<AlarmSixMessageInfo> list) {
        this.mList = list;
//        sId.clear();
//        for (int i = 0; i < list.size(); i++) {
//            AlarmSixMessageInfo detailInfo = mList.get(i);
//            String dealStatus = detailInfo.getDeal_status();
//            if (CodeConstant.STATUS_PLAN_HANDLE.equals(dealStatus)) {
//                sId.add(detailInfo.getSid());
//            } else if (CodeConstant.STATUS_OVERDUE_PLAN_HANDLE.equals(dealStatus)) {
//                sId.add(detailInfo.getSid());
//            }
//        }
        initDate(list);
        notifyDataSetChanged();
    }
    //获取选中id
    public List<String> getSid() {
        return sId;
    }
    //获取选中id
    public void clearSid() {
        sId.clear();
    }
    public void setSid() {
        sId.clear();
        for (int i = 0; i < mList.size(); i++) {
            AlarmSixMessageInfo detailInfo = mList.get(i);
            String dealStatus = detailInfo.getDeal_status();
            if (CodeConstant.STATUS_PLAN_HANDLE.equals(dealStatus)) {
                sId.add(detailInfo.getSid());
            } else if (CodeConstant.STATUS_OVERDUE_PLAN_HANDLE.equals(dealStatus)) {
                sId.add(detailInfo.getSid());
            }
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();//初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_alarm_six_info_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.alarm_six_info_layout);
            holder.check = (CheckBox) convertView.findViewById(R.id.alarm_six_info_check);
            holder.time = (TextView) convertView.findViewById(R.id.alarm_six_info_time);
            holder.type = (TextView) convertView.findViewById(R.id.alarm_six_info_type);
            holder.status = (TextView) convertView.findViewById(R.id.alarm_six_info_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AlarmSixMessageInfo detailInfo = mList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }
        String dealStatus = detailInfo.getDeal_status();
        String dealText = "";
        if (CodeConstant.STATUS_PLAN_HANDLE.equals(dealStatus)) {
            dealText = "待处理";
            holder.check.setClickable(true);
        } else if (CodeConstant.STATUS_OVERDUE_UNHANDLED.equals(dealStatus)) {
            dealText = "逾期未处理";
            holder.check.setClickable(false);
        } else if (CodeConstant.STATUS_HANDLED.equals(dealStatus)) {
            dealText = "已处理";
            holder.check.setClickable(false);
        } else if (CodeConstant.STATUS_THIRD.equals(dealStatus)) {
            dealText = "第三方处理";
            holder.check.setClickable(false);
        } else if (CodeConstant.STATUS_OVERDUE_PLAN_HANDLE.equals(dealStatus)) {
            dealText = "延期处理";
            holder.check.setClickable(true);
        } else if (CodeConstant.STATUS_OVERDUE_HANDLED.equals(dealStatus)) {
            dealText = "延期已处理";
            holder.check.setClickable(false);
        }
        holder.time.setText(detailInfo.getAlarmtime());
        holder.type.setText(detailInfo.getAlarm_type_name());
        holder.status.setText(dealText);
        if(holder.check.isClickable()){
            holder.check.setChecked(getIsSelected().get(position));
        }else{
            holder.check.setChecked(false);
        }
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())return;
                if (isChecked) {
                    sId.add(mList.get(position).getSid());
                } else {
                    sId.remove(mList.get(position).getSid());
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        LinearLayout layout;
        CheckBox check;//选择状态
        TextView time;//时间
        TextView type;//异常类型
        TextView status;//处理状态
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

}
