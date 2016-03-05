package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmOverReport;

import java.util.List;

/**
 * Created by Administrator on 2015/11/23.
 */
public class AttentionOverReportItemAdapter extends BaseAdapter {
    private List<AlarmOverReport> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionOverReportItemAdapter(Context context, List<AlarmOverReport> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AlarmOverReport> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.layout_attention_report_over_item, null);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.item_attention_over_report_layout);
            holder.pollSourceName = (TextView) convertView.findViewById(R.id.item_attention_over_report_name);
            holder.overTimes = (TextView) convertView.findViewById(R.id.item_attention_over_report_overtimes);
            holder.time = (TextView) convertView.findViewById(R.id.item_attention_over_report_time);
            holder.pollutantName = (TextView) convertView.findViewById(R.id.item_attention_over_report_pollutant);
            holder.overChroma = (TextView) convertView.findViewById(R.id.item_attention_over_report_overchroma);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlarmOverReport detailInfo = mList.get(position);

        holder.pollSourceName.setText(detailInfo.getPoll_source_name());
        holder.overTimes.setText("超标倍数："+detailInfo.getOvertimes());
        holder.overChroma.setText("超标浓度："+detailInfo.getOverchroma());
        holder.time.setText(detailInfo.getBegin_time());
        holder.pollutantName.setText("污染物："+detailInfo.getPollutant_name());

        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_green));
        }
        return convertView;
    }


    class ViewHolder {
        RelativeLayout layout;
        TextView pollSourceName;//工厂名称
        TextView overTimes;//超标倍数
        TextView time;//时间
        TextView pollutantName;//超标污染物
        TextView overChroma;//超标浓度
    }
}
