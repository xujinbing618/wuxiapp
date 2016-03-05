package com.magus.enviroment.ep.activity.knowledge;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工况介绍
 */
public class WorkIntroActivity extends SwipeBackActivity {
    private CustomActionBar mActionBar;

    private ExpandableListView mExpandlistview;

    private List<String> parent;
    private Map<String, List<String>> map;
    private Drawable img_down, img_up;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_intro);
        initActionBar();
        initData();
        initView();

    }

    private void initData() {
        String[] args = getResources().getStringArray(R.array.work_intro);
        parent = new ArrayList<String>();
        for (String s : args) {
            parent.add(s);
        }

        map = new HashMap<String, List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add("远程监控");
        list1.add("判断在线数据准确性");
        list1.add("分析结果");
        map.put("作用", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("物料平衡");
        list2.add("关联关系");
        list2.add("逻辑关系");
        list2.add("环保检查经验");
        map.put("原理", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("异常规则");
        list3.add("环保意义");
        map.put("工况异常", list3);
    }

    private void initView() {
        img_down = getResources().getDrawable(R.mipmap.group_down);
        img_down.setBounds(0, 0, img_down.getMinimumWidth(), img_down.getMinimumHeight());
        img_up = getResources().getDrawable(R.mipmap.group_up);
        img_up.setBounds(0, 0, img_up.getMinimumWidth(), img_up.getMinimumHeight());
        mExpandlistview = (ExpandableListView) findViewById(R.id.main_expandablelistview);
        mExpandlistview.setGroupIndicator(null);
        mExpandlistview.setAdapter(new MyAdaper());
        mExpandlistview.setOnChildClickListener(new MyChildClickListener());
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(WorkIntroActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

    class MyAdaper extends BaseExpandableListAdapter {


        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);

            return (map.get(key).get(childPosition));
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String key = WorkIntroActivity.this.parent.get(groupPosition);
            String info = WorkIntroActivity.this.map.get(key).get(childPosition);
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) WorkIntroActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, null);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.ll_children);
                holder.tv = (TextView) convertView.findViewById(R.id.second_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv.setText(info);
            return convertView;
        }


        @Override
        public Object getGroup(int groupPosition) {

            return WorkIntroActivity.this.parent.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return WorkIntroActivity.this.parent.size();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String info = WorkIntroActivity.this.parent.get(groupPosition);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) WorkIntroActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.ll_parent);
                holder.tv = (TextView) convertView.findViewById(R.id.parent_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(info);
            if (isExpanded) {
                holder.tv.setCompoundDrawables(null, null, img_up, null);

            } else {
                holder.tv.setCompoundDrawables(null, null, img_down, null);
            }


            return convertView;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView tv;
        }
    }

    class MyChildClickListener implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            String key = WorkIntroActivity.this.parent.get(groupPosition);

            Intent intent = new Intent();
            intent.setClass(WorkIntroActivity.this, DetailKnowledgeActivity.class);

            if (key.equals("作用")) {
                intent.putExtra("flag","img");
                switch (childPosition) {
                    case 0:
                        intent.putExtra("flag_img","control");
                        intent.putExtra("title", "远程监控");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("flag_img","judge");
                        intent.putExtra("title", "异常判断");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("flag_img","analyse");
                        intent.putExtra("title", "分析结果");
                        startActivity(intent);
                        break;
                }

            } else if (key.equals("原理")) {
                intent.putExtra("flag","txt");
                switch (childPosition) {
                    case 0:
                        intent.putExtra("title", "物料平衡");
                        intent.putExtra("text", "\t\t以脱硫系统为例：根据物质守恒的原理，我们从脱硫系统中提取了烟气平衡模型、浆液平衡模型，运用到工况监控系统。");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("title", "关联关系");
                        intent.putExtra("text", "\t\t以燃煤电厂为例：在燃煤燃烧理论中，有一些参数是有关联关系的，比如燃煤量、烟气量等，将这些关联关系融入到工况监控系统中，可以发现一些异常情况。"
                        );
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("title", "逻辑关系");
                        intent.putExtra("text", "\t\t以脱硫系统为例：在脱硫系统中，多个过程参数之间有着复杂的逻辑关系，工况系统实时对这些参数进行比对，发现治污设施的运行异常。");
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra("title", "环保检查经验");
                        intent.putExtra("text", "\t\t在大量的污染源企业检查中，环保部门积累了很多监管经验，已经发现的企业对在线监控数据的作假手段，都可以成为工况监控系统自动化判断的规则。");
                        startActivity(intent);
                        break;
                }
            } else if (key.equals("工况异常")) {
                switch (childPosition) {

                    case 0:
                        intent.putExtra("flag","tab");

                        intent.putExtra("title", "异常规则");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("flag","tab_protect");
                        intent.putExtra("title", "环保意义");
                        startActivity(intent);
                        break;
                }

            }
            return false;
        }
    }

}
