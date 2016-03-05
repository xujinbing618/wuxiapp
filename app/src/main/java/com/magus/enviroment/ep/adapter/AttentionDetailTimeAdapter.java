package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmDetailInfo;

import java.util.List;

/**
 * Created by pau on 15/6/5.
 */
public class AttentionDetailTimeAdapter extends BaseAdapter {
    private static final String TAG = "DetailTimeAdapter";

    private List<AlarmDetailInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionDetailTimeAdapter(Context context, List<AlarmDetailInfo> list) {
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();//初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_attention_detail_time_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_detail_layout);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.note = (TextView) convertView.findViewById(R.id.note);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlarmDetailInfo detailInfo = mList.get(position);


        holder.time.setText(detailInfo.getBeginDate()+"-\r\n"+detailInfo.getEndDate());
        holder.type.setText(detailInfo.getAlarmTypeName());
        String dealStatus = detailInfo.getDealStatus();
        String dealText = "";
        if ("0".equals(dealStatus)) {
            dealText = "待处理";
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
            detailInfo.setIsChecked(false);
        } else if ("1".equals(dealStatus)) {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
            dealText = "逾期未处理";
            detailInfo.setIsChecked(true);
            holder.checkBox.setEnabled(false);
        } else if ("2".equals(dealStatus)) {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
            dealText = "已处理";
            detailInfo.setIsChecked(true);
            holder.checkBox.setEnabled(false);
            holder.layout.setClickable(false);
        } else if ("3".equals(dealStatus)) {
            dealText = "第三方处理";
        } else if ("4".equals(dealStatus)) {
            dealText = "延期处理";
        } else if ("5".equals(dealStatus)) {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
            detailInfo.setIsChecked(true);
            holder.checkBox.setEnabled(false);
            dealText = "延期已处理";
        }
        holder.note.setText(dealText);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailInfo.isChecked()) {
                    holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
                    detailInfo.setIsChecked(false);
                } else {
                    holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
                    detailInfo.setIsChecked(true);
                }
            }
        });

        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_green));
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout layout;
        ImageView checkBox;
        TextView time;
        TextView note;
        TextView type;
    }
}
