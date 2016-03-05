/*
package com.magus.enviroment.ep.activity.my;

import android.os.Bundle;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONObject;

*/
/**
 * 新版本说明
 *//*

public class MySystemSettingVersionExplain extends SwipeBackActivity {
    private TextView my_system_settings_about_us_txt;
    private CustomActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_system_seting_about_us);
        initActionBar();
        my_system_settings_about_us_txt = (TextView) findViewById(R.id.my_system_settings_about_us_txt);
        sendRequest();
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MySystemSettingVersionExplain.this);
        mActionBar.getMiddleTextView().setText("新版本说明");
    }

    */
/**
     * 请求
     *//*

    private void sendRequest() {
        String url = URLConstant.HEAD_URL + URLConstant.URL_ABOUT_US;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);
                    if (o.has("resultCode")) {
                        if (o.getString("resultCode").equals("true")) {
                            if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("{}")) {
                                JSONObject jsonObject = new JSONObject(o.getString("resultEntity"));
                                if (jsonObject.has("message")) {
                                    my_system_settings_about_us_txt.setText(jsonObject.getString("message"));
                                }
                            }
                        }
                    }

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getRequestQueue().add(request);

    }

}

*/
