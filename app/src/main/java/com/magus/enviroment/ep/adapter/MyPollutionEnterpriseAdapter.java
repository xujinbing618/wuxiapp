package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AttentionEnterprise;
import com.magus.enviroment.ep.bean.AttentionZone;
import com.magus.enviroment.ep.dao.EnterpriseManagerDao;
import com.magus.enviroment.ep.dao.ZoneManagerDao;

import java.util.HashSet;
import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class MyPollutionEnterpriseAdapter extends BaseAdapter {
    private static final String TAG = "EnterpriseAdapter";

    private List<AttentionEnterprise> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private boolean mIsSelectAll = false; //是否全选
    private boolean mIsSelletAllPressed = false;//是否按下全选

    private EnterpriseManagerDao mEnterpriseManagerDao;
    private ZoneManagerDao mZoneManagerDao;

    private String mZoneId;
    private String mZoneName;

    public MyPollutionEnterpriseAdapter(Context context, List<AttentionEnterprise> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        mEnterpriseManagerDao = EnterpriseManagerDao.getInstance(context);
        mZoneManagerDao = ZoneManagerDao.getInstance(context);

    }

    /**
     * @param list
     * @param isSelectAll        是否全选
     * @param isSelletAllPressed 是否按下全选
     */
    public void setList(List<AttentionEnterprise> list, boolean isSelectAll, boolean isSelletAllPressed, String zoneId, String zoneName) {
        if (list != null) {
            this.mList = list;
            this.mIsSelectAll = isSelectAll;
            this.mIsSelletAllPressed = isSelletAllPressed;
            this.mZoneId = zoneId;
            this.mZoneName = zoneName;
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
            convertView = mLayoutInflater.inflate(R.layout.item_pollution_enterprise_layout, null);
            holder.cityName = (TextView) convertView.findViewById(R.id.enterprise_name);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.is_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AttentionEnterprise enterprise = mList.get(position);
        holder.cityName.setText(enterprise.getPollName());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<String> enterpriseIds = mEnterpriseManagerDao.queryEnterpriseIds();
                android.util.Log.e(TAG, enterpriseIds.toString());
                if (enterpriseIds.contains(enterprise.getPollId())) {
                    holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
                    mEnterpriseManagerDao.deleteEnterprise(enterprise);
                } else {
                    holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
                    mEnterpriseManagerDao.insertEnterprise(enterprise);
                }
                AttentionZone zone = new AttentionZone();
                zone.setZoneId(mZoneId);
                zone.setZoneName(mZoneName);
                zone.setStatus("2");
                mZoneManagerDao.updateZoneStatus(zone);
            }
        });

        if (enterprise.isChecked()) {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
        } else {
            holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
        }
        if (mIsSelletAllPressed) {
            if (mIsSelectAll) {
                holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.added_city));
            } else {
                holder.checkBox.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.add_city));
            }
        }
        return convertView;
    }


    class ViewHolder {
        TextView cityName;
        ImageView checkBox;
    }
}
