package com.magus.enviroment.ep.util;

import android.os.Environment;
import android.util.Log;

import com.magus.enviroment.ep.constant.URLConstant;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author huarizhong
 * Date 2015/1/28
 * Time 11:03
 */
public class MyDownload implements Runnable {
    private final String TAG = "MyDownload";
    private String mUrl;
    private MyDownloadHandler mHandler;
    public String mPath = Environment.getExternalStorageDirectory()
            + "/Environment/download/";
    public static boolean isCancel = false;

    public MyDownload(String url, MyDownloadHandler handler) {
        System.out.println(url);
        this.mUrl = url;
        this.mHandler = handler;
    }

    @Override
    public void run() {

        String[] name = mUrl.split("/");
        mPath = mPath + name[name.length - 1];
        File file = new File(mPath);

        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection)
                    url.openConnection();
            connection.setRequestMethod("GET");
            //是否允许输入
            connection.setDoInput(true);
            //是否允许输出
            connection.setDoOutput(false);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            //实现连接
            connection.connect();

            Log.e(TAG, "connection.getResponseCode()=" + connection.getResponseCode());
            //连接失败
            if (connection.getResponseCode() != 200) {
                if (mHandler != null) {
                    mHandler.onFailed();
                    connection.disconnect();
                }
                return;
            }
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                //以下为下载操作
                byte[] bytes = new byte[1];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(baos);
                int len = is.read(bytes);
                Log.e(TAG, len + "");
                try {
                    while (len > 0 && !isCancel) {
                        bos.write(bytes);
                        len = is.read(bytes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.onFailed();
                }
                bos.close();
                if (isCancel){
                    if (mHandler != null) {
                        mHandler.onFailed();
                    }
                }else {

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(baos.toByteArray());
                    fos.close();
                    if (mHandler != null) {
                        mHandler.onSuccess(file);
                    }
                }

                //关闭网络连接
                connection.disconnect();
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            mHandler.onFailed();
        }
    }

}