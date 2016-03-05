package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.PushBean;

import java.util.List;
/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.adapter
 * 2015-15/5/6-下午1:52.
 */
public class MyMessageAdapter extends BaseAdapter {
    private static final String TAG = "MyMessageAdapter";

    private List<PushBean> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public MyMessageAdapter(Context context, List<PushBean> list){
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<PushBean> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_my_message_layout, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.item_my_message_layout);
            holder.messageTitle = (TextView) convertView.findViewById(R.id.my_message_title);
            holder.messageContent = (TextView) convertView.findViewById(R.id.my_message_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PushBean detailInfo = mList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_gray));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.me_message_background_white));
        }
        holder.messageTitle.setText(detailInfo.getAlarm_type_name());
        holder.messageContent.setText(detailInfo.getAlarm_detal());
        return convertView;
    }




    class ViewHolder {
        LinearLayout layout;
        TextView messageTitle;
        TextView messageContent;
    }
}
