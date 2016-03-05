package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AttentionZone;
import com.magus.enviroment.ep.dao.ZoneManagerDao;
import com.magus.enviroment.global.log.Log;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class MyPollutionCityAdapter extends BaseAdapter {
    private static final String TAG = "MyPollutionAdapter";
    private List<AttentionZone> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private boolean mIsSelectAll = false; //是否全选
    private boolean mIsSelletAllPressed = true;//是否按下全选

    private ZoneManagerDao mManagerDao;

    public MyPollutionCityAdapter(Context context, List<AttentionZone> list) {

        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        mManagerDao = ZoneManagerDao.getInstance(context);
    }

    /**
     * @param list
     * @param isSelectAll        是否全选
     * @param isSelletAllPressed 是否按下全选
     */
    public void setList(List<AttentionZone> list, boolean isSelectAll, boolean isSelletAllPressed) {
        for (int i=0;i<list.size();i++){
            System.out.println("城市名称："+list.get(i).getZoneName()+i);
        }
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
            convertView = mLayoutInflater.inflate(R.layout.item_pollution_city_layout, null);
            holder.cityName = (TextView) convertView.findViewById(R.id.city_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AttentionZone zone = mList.get(position);
        holder.cityName.setText(mList.get(position).getZoneName());
        if (!zone.isChecked()) {
            zone.setStatus("0");
        } else {
            zone.setStatus("1");
        }
        mManagerDao.updateZoneStatus(zone);

        return convertView;
    }


    class ViewHolder {
        TextView cityName;
       // ImageView checkBox;
    }
}
