package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.magus.enviroment.ep.bean.AttentionEnterprise;

import java.util.ArrayList;
import java.util.List;

public class MySpinnerAdapter extends ArrayAdapter<String> {
    private List<AttentionEnterprise> mList = new ArrayList<AttentionEnterprise>();

    public MySpinnerAdapter(Context context,List<AttentionEnterprise> list) {
        super(context, android.R.layout.simple_spinner_item);
        this.mList.clear();
        this.mList.addAll(list);
        this.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    public void setList(List<AttentionEnterprise> list) {
        if (list != null) {
            this.mList.clear();
            this.mList.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    public List<AttentionEnterprise> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public String getItem(int position) {
        return this.mList.get(position).getPollName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
