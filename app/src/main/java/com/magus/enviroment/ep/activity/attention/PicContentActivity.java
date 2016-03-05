package com.magus.enviroment.ep.activity.attention;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.ImageRequest;

import uk.co.senab.photoview.PhotoView;

;

/**
 * 截图报警详情
 * Created by pau on 15/7/22.
 */
public class  PicContentActivity extends SwipeBackActivity {
    private static final String TAG = "PicContentActivity";

    private PhotoView photo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageRequest(URLConstant.HEAD_URL + getIntent().getStringExtra("alarm_path"));
        photo = new PhotoView(this);
        setContentView(photo);
    }

/**
     * 根据url显示图片
     *
     * @param url
     */
    private void imageRequest(String url) {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                photo.setImageBitmap(bitmap);
            }
        }, width, height, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                photo.setBackgroundDrawable(getResources().getDrawable(R.drawable.faild));
            }
        });
        MyApplication.getRequestQueue().add(request);
    }


}
