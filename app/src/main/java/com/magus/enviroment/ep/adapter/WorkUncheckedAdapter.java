package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;

import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.adapter
 * 2015-15/4/21-上午10:01.
 */
public class WorkUncheckedAdapter extends BaseAdapter{
    private List<String> mList;
    private LayoutInflater mLayoutInflater ;
    private Context mContext;


    public WorkUncheckedAdapter(Context context,List<String> list){
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<String> list){
        if (list!=null){
            this.mList = list;
            notifyDataSetChanged();
        }else {
            return ;
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
            holder = new ViewHolder();
            //初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_work_layout, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.phone = (ImageView) convertView.findViewById(R.id.phone);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.message = (ImageView) convertView.findViewById(R.id.message);
            holder.description = (TextView) convertView.findViewById(R.id.description);
//            holder.name.setText(mList.get(position));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


    class ViewHolder{
        TextView name ;
        TextView type ;
        ImageView phone;
        TextView number;
        TextView description;
        ImageView message;
    }
}
