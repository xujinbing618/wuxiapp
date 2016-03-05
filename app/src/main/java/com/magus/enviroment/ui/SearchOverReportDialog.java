package com.magus.enviroment.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.magusutils.DateUtil;

/**
 * Created by Administrator on 2015/11/27.
 */
public class SearchOverReportDialog extends Dialog {
    private TextView search_over_report_dialog_time;//月份
    private ImageView previous_month;//前一个月
    private ImageView after_month;//后一个月
    public Button search_over_report_dialog_sbumit;//确定按钮
    private static int ID_PREVIOUS_DAY = 0;
    private static int ID_AFTER_DAY = 1;
    private String dayString;//当前月份
    private Context context;
    public SearchOverReportDialog(Context context) {
        super(context);
        this.context=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_over_report_dialog);
        initView();
    }
    private void initView() {
        dayString = DateUtil.getCurrentMon();
        search_over_report_dialog_time = (TextView) findViewById(R.id.search_over_report_dialog_time);
        search_over_report_dialog_time.setText(dayString);

        previous_month = (ImageView) findViewById(R.id.previous_month);
        after_month = (ImageView) findViewById(R.id.after_month);
        previous_month.setOnClickListener(onClickListener);
        after_month.setOnClickListener(onClickListener);
        search_over_report_dialog_sbumit=(Button)findViewById(R.id.search_over_report_dialog_sbumit);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.previous_month:
                    changeDate(ID_PREVIOUS_DAY);
                    break;
                case R.id.after_month:
                    changeDate(ID_AFTER_DAY);
                    break;
                default:
                    break;
            }
        }
    };

    //改变日期，图表会发生变化
    private void changeDate(int id) {
        if (id == ID_PREVIOUS_DAY) {
            dayString = DateUtil.getSpecifiedMonBefore(dayString);
            search_over_report_dialog_time.setText(dayString);
        } else if (id == ID_AFTER_DAY) {
            dayString = DateUtil.getSpecifiedMonAfter(dayString);
//            android.util.Log.e(TAG,dayString);
            if (dayString.compareTo(DateUtil.getCurrentMon()) > 0) { //比较日期 不能超过当前日期
                Toast.makeText(context, "超过当前日期", Toast.LENGTH_SHORT).show();
                dayString = DateUtil.getCurrentMon();
            } else {
                search_over_report_dialog_time.setText(dayString);
            }
        }

    }
    public String getMonth(){
        return search_over_report_dialog_time.getText().toString();
    }
}


