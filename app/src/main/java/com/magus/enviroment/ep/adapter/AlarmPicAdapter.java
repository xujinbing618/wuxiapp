package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.activity.attention.PicContentActivity;
import com.magus.enviroment.ep.bean.AlarmPic;

import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
public class AlarmPicAdapter extends BaseAdapter{
    private static final String TAG = "AlarmPicAdapter";
    private LayoutInflater mLayoutInflater;
    private List<AlarmPic> mList;
    private Context mContext;
    public AlarmPicAdapter(Context context, List<AlarmPic> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }
    public void setList(List<AlarmPic> list) {
       // if (list != null) {
            this.mList = list;
            notifyDataSetChanged();
        //} else {
        //    return;
        //}
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();//初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_alarm_pic, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_alarm_pic_layout);
            holder.alarm_address = (TextView) convertView.findViewById(R.id.attention_alarm_pic_layout_alarm_address);
            holder.alarmTypeName = (TextView) convertView.findViewById(R.id.attention_alarm_pic_layout_alarmTypeName);
            holder.alarmTime=(TextView)convertView.findViewById(R.id.attention_alarm_pic_layout_alarmTime);
            holder.btn_alarm_cliled=(Button)convertView.findViewById(R.id.btn_alarm_cliled);
            holder.btn_alarm_cliled.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AlarmPic detailInfo = mList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }

        holder.alarm_address.setText(detailInfo.getAlarm_address());
        holder.alarmTypeName.setText(detailInfo.getAlarm_type_name());
        holder.alarmTime.setText(detailInfo.getAlarm_time());
        holder.btn_alarm_cliled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmPic info = mList.get(position);
                Intent intent = new Intent();
                intent.setClass(mContext, PicContentActivity.class);
                intent.putExtra("alarm_path", info.getAlarm_path());
                intent.putExtra("alarm_desc", info.getAlarm_desc());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder {
        LinearLayout layout;
        TextView alarm_address;
        TextView alarmTypeName;
        TextView alarmTime;
        Button btn_alarm_cliled;
    }
}
