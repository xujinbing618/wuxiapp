package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmOverReport;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class AttentionReportOverAdapter extends BaseAdapter {
    private static final String TAG = "AttentionReportOverAdapter";

    private List<AlarmOverReport> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionReportOverAdapter(Context context, List<AlarmOverReport> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_attention_report_over_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.attention_report_over_layout);
            holder.facilityName = (TextView) convertView.findViewById(R.id.facility_name);
            holder.pollutantName = (TextView) convertView.findViewById(R.id.pollutant_name);
//            holder.overTimes = (TextView) convertView.findViewById(R.id.over_times);
//            holder.timeCount = (TextView) convertView.findViewById(R.id.time_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlarmOverReport detailInfo = mList.get(position);

        holder.facilityName.setText(detailInfo.getPoll_source_name()+"("+detailInfo.getFacility_name()+")");
        holder.pollutantName.setText(detailInfo.getPollutant_name());

//        holder.overTimes.setText(detailInfo.getOverTimes());
//        holder.timeCount.setText(""+detailInfo.getTimeCou());

        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_green));
        }
        return convertView;
    }


    class ViewHolder {
        LinearLayout layout;
//        TextView pollSourceName;
  //      TextView overTimes;//超标倍数
 //       TextView timeCount;//总时长
        TextView pollutantName;//超标污染物
        TextView facilityName;//设施名称
    }
}
