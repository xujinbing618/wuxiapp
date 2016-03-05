package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmCauseInfo;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class AttentionCauseAdapter extends BaseAdapter {
    private static final String TAG = "AttentionCauseAdapter";

    private List<AlarmCauseInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AttentionCauseAdapter(Context context, List<AlarmCauseInfo> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AlarmCauseInfo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_attention_cause_layout, null);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.check_box);
            holder.causeName = (TextView) convertView.findViewById(R.id.cause_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlarmCauseInfo alarmCauseInfo = mList.get(position);
        if (alarmCauseInfo.isChecked()) {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
        } else {

            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
        }
        holder.causeName.setText(alarmCauseInfo.getDicName());
        return convertView;
    }

    class ViewHolder {
        ImageView checkBox;
        TextView causeName;

    }
}
