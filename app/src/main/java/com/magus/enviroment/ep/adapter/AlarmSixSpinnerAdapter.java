package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magus.enviroment.ep.bean.AttentionEnterprise;

import java.util.List;

/**
 * Created by Administrator on 2015/10/27.
 */
public class AlarmSixSpinnerAdapter extends BaseAdapter {
    private List<AttentionEnterprise> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int code;

    public AlarmSixSpinnerAdapter(Context context, List<AttentionEnterprise> list,int code) {
        this.mContext = context;
        this.mList = list;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.code=code;

    }
    public void setList(List<AttentionEnterprise> list) {
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
            convertView = mLayoutInflater.inflate(android.R.layout.simple_spinner_item, null);
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(mList.get(position).getPollName());

        return convertView;
    }
}
