package com.magus.enviroment.ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.AlarmProblem;

import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class ServiceNormalQuestionAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<AlarmProblem> mList;
    private Context mContext;

    public ServiceNormalQuestionAdapter(Context context, List<AlarmProblem> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    public void setList(List<AlarmProblem> list) {
        // if (list != null) {
        this.mList = list;
        notifyDataSetChanged();
        //} else {
        //    return;
        //}
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
            convertView = mLayoutInflater.inflate(R.layout.item_normal_question_layout, null);
            holder.problem = (TextView) convertView.findViewById(R.id.nomal_question_problem);
            holder.answer = (TextView) convertView.findViewById(R.id.nomal_question_answer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AlarmProblem detailInfo = mList.get(position);
        holder.problem.setText(detailInfo.getProblem());
        holder.answer.setText("回答："+detailInfo.getAnswer());
        return convertView;
    }

    class ViewHolder {

        TextView problem;//问题
        TextView answer;//回答

    }
}
