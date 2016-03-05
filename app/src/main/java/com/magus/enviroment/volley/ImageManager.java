package com.magus.enviroment.volley;

import android.widget.ImageView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.volley.toolbox.ImageLoader;


/**
 * Author huarizhong
 * Date 2015/3/11
 * Time 11:31
 */
public class ImageManager {
    /*
     *图片下载
     */
    public static void download(String url, ImageView imageView) {
        ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(), new VolleyImageCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.ic_launcher, R.drawable.nav_home);
        imageLoader.get(url, listener);
    }
}
