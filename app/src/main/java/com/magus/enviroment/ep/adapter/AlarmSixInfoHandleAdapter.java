package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmSixInfoHandle;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class AlarmSixInfoHandleAdapter extends BaseAdapter {
    private List<AlarmSixInfoHandle> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String checkId;
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();//用于记录每个RadioButton的状态，

    public AlarmSixInfoHandleAdapter(Context context, List<AlarmSixInfoHandle> list) {
        this.mContext = context;
        this.list = list;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<AlarmSixInfoHandle> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        } else {
            return;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.alarm_six_info_handle_adapter_layout, null);
            // holder.radioButton = (RadioButton) convertView.findViewById(R.id.rb_dic_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AlarmSixInfoHandle detailInfo = list.get(position);
        final RadioButton radio = (RadioButton) convertView.findViewById(R.id.rb_dic_name);
        holder.radioButton = radio;

        holder.radioButton.setText(detailInfo.getDic_name());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);

                }
                states.put(String.valueOf(position), radio.isChecked());
                AlarmSixInfoHandleAdapter.this.notifyDataSetChanged();

            }
        });
        boolean res = false;
        if (states.get(String.valueOf(position)) == null
                || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else
            res = true;
        holder.radioButton.setChecked(res);

        return convertView;
    }
    class ViewHolder {
        RadioButton radioButton;
    }
}


