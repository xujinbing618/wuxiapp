package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmTypeInfo;
import com.magus.enviroment.ep.dao.AlarmTypeDao;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class MyAlarmTypeAdapter extends BaseAdapter {
    private static final String TAG = "MyPollutionAdapter";
    private List<AlarmTypeInfo> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private boolean mIsSelectAll = false; //是否全选
    private boolean mIsSelletAllPressed = true;//是否按下全选
    private AlarmTypeDao mAlarmTypeDao;


    public MyAlarmTypeAdapter(Context context, List<AlarmTypeInfo> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAlarmTypeDao = AlarmTypeDao.getInstance(context);
    }

    /**
     * @param list
     * @param isSelectAll        是否全选
     * @param isSelletAllPressed 是否按下全选
     */
    public void setList(List<AlarmTypeInfo> list, boolean isSelectAll, boolean isSelletAllPressed) {
        if (list != null) {
            this.mList = list;
            this.mIsSelectAll = isSelectAll;
            this.mIsSelletAllPressed = isSelletAllPressed;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();//初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_alarm_type_layout, null);
            holder.typeName = (TextView) convertView.findViewById(R.id.type_name);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.is_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlarmTypeInfo alarmTypeInfo = mList.get(position);
        holder.typeName.setText(alarmTypeInfo.getAlarmTypeName());
        if ("true".equals(alarmTypeInfo.getChecked())) {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
        } else {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("true".equals(alarmTypeInfo.getChecked())) {
                    holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
                    alarmTypeInfo.setChecked("false");
                    mAlarmTypeDao.insertAlarmType(alarmTypeInfo);
                } else {
                    holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
                    alarmTypeInfo.setChecked("true");
                    mAlarmTypeDao.insertAlarmType(alarmTypeInfo);
                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView typeName;
        ImageView checkBox;
    }


}
