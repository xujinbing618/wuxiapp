package com.magus.enviroment.volley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.magus.volley.toolbox.ImageLoader;

/**
 * Volley图片缓存
 * Author huarizhong
 * Date 2015/3/11
 * Time 10:59
 */
public class VolleyImageCache implements ImageLoader.ImageCache {

    private LruCache<String,Bitmap> mCache;

    public VolleyImageCache(){
        //这个取单个应用最大使用内存的1/8
        int maxSize=(int)Runtime.getRuntime().maxMemory()/8;
//        int maxSize = 10*1024*1024;
        mCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //这个方法一定要重写，不然缓存没有效果
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String key) {
        return mCache.get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        mCache.put(key, bitmap);
    }
}
