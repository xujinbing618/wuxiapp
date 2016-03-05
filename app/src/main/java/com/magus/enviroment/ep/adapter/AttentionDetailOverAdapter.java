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
 * Created by pau on 15/6/5.
 */
public class AttentionDetailOverAdapter extends BaseAdapter {
    private static final String TAG = "DetailTimeAdapter";

    private List<AlarmDetailInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionDetailOverAdapter(Context context, List<AlarmDetailInfo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_attention_detail_over_layout, null);
            holder.layout = (LinearLayout) convertView.
                    findViewById(R.id.attention_detail_layout);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.note = (TextView) convertView.findViewById(R.id.note);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlarmDetailInfo detailInfo = mList.get(position);


        holder.time.setText(detailInfo.getBeginDate()+"-\r\n"+detailInfo.getEndDate());
        holder.type.setText(detailInfo.getAlarmTypeName());
        String dealStatus = detailInfo.getDealStatus();
        if ("0".equals(dealStatus)) {
            holder.note.setText("待处理");
        } else if ("1".equals(dealStatus)) {
            holder.note.setText("逾期未处理");
        } else if ("2".equals(dealStatus)) {
            holder.note.setText("已处理");
        } else if ("3".equals(dealStatus)) {
            holder.note.setText("第三方处理");
        } else if ("4".equals(dealStatus)) {
            holder.note.setText("延期处理");
        } else if ("5".equals(dealStatus)) {
            holder.note.setText("延期已处理");
        }

        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_green));
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout layout;
        TextView time;
        TextView note;
        TextView type;
    }
}
