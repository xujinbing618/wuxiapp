package com.magus.enviroment.ep.activity.knowledge;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;

/**
 * 环保知识详细页面
 */
public class DetailKnowledgeActivity extends SwipeBackActivity {

    private CustomActionBar mActionBar;
    private String title;
    private String text;
    int width;
    private TextView tv;

    private ScrollView scrollTxt;
    private ScrollView scrollTab;
    private ImageView img;
    private TableLayout tab;
    String flag;
    String flag_img;
    private Drawable img1,img2,img3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fog);
        initActionBar();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        width = dm.widthPixels;
        initView();
        initData();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv_detail);
        //根据屏幕调整文字大小
        tv.setLineSpacing(0f, 1.5f);
        tv.setTextSize(8 * (float) width / 320f);

        scrollTxt = (ScrollView) findViewById(R.id.text);
        scrollTab = (ScrollView) findViewById(R.id.table);
        img = (ImageView) findViewById(R.id.image);
        tab = (TableLayout) findViewById(R.id.tab_protect);
        img1 = getResources().getDrawable(R.drawable.work_control);
        img2 = getResources().getDrawable(R.drawable.work_judge);
        img3 = getResources().getDrawable(R.drawable.work_analyse);
    }

    private void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        text = intent.getStringExtra("text");
        flag = intent.getStringExtra("flag");

        if(flag.equals("img")){
            img.setVisibility(View.VISIBLE);
            flag_img = intent.getStringExtra("flag_img");
            if(flag_img.equals("control")){
                img.setImageDrawable(img1);

            }else if(flag_img.equals("judge")){
                img.setImageDrawable(img2);
            }else if(flag_img.equals("analyse")){
                img.setImageDrawable(img3);
            }

        }else if(flag.equals("txt")){
            scrollTxt.setVisibility(View.VISIBLE);
        }else if(flag.equals("tab")){
            scrollTab.setVisibility(View.VISIBLE);

        }else  if(flag.equals("tab_protect")){
            tab.setVisibility(View.VISIBLE);
        }
        tv.setText(text);
        mActionBar.getMiddleTextView().setText(title);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(DetailKnowledgeActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

}
