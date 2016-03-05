package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmGridPercentage;
import com.magus.enviroment.ep.bean.DealRate;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class AttentionAbnormalAdapter extends BaseAdapter {
    private static final String TAG = "AttentionAbnormalAdapter";

    private List<AlarmGridPercentage> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionAbnormalAdapter(Context context, List<AlarmGridPercentage> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

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
            convertView = mLayoutInflater.inflate(R.layout.item_attention_abnormal_statistics, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_abnormal_layout);
            holder.area = (TextView) convertView.findViewById(R.id.area);
            holder.lost = (TextView) convertView.findViewById(R.id.lost);
            holder.stop = (TextView) convertView.findViewById(R.id.stop);
            holder.over = (TextView) convertView.findViewById(R.id.over);
            holder.stop2 = (TextView) convertView.findViewById(R.id.stop2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }
        AlarmGridPercentage rateInfo = mList.get(position);
        holder.area.setText(rateInfo.getPoll_source_name());
        List<DealRate> list = rateInfo.getDealRates();

        for (int i = 0; i < list.size(); i++) {
            String code = list.get(i).getAlarmCode();
            String rate = list.get(i).getRate();
            if ("7".equals(code)) {//数据丢失
                holder.lost.setText(rate);
            } else if ("5".equals(code)) { //机组停运
                holder.stop.setText(rate);
            } else if ("14".equals(code)) {//数据超标
                holder.over.setText(rate);
            } else if ("9".equals(code)) {//治污设施停运
                holder.stop2.setText(rate);
            }
        }
        return convertView;
    }


    class ViewHolder {
        LinearLayout layout;
        TextView area;
        TextView lost;//数据丢失    code:7
        TextView stop;//机组停运    code:5
        TextView over;//数据超标    code:14
        TextView stop2;//治污设施停运 code:9
    }
}
