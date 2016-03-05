package com.magus.enviroment.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pau on 15/6/28.
 */
public class HttpPostRequest implements Runnable {

    private String TAG="HttpPostRequest";
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            String requestUrl = "http://192.168.199.180:8080/SingleServer/User/login.do";
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 数据输出流
            DataOutputStream dos = new DataOutputStream(
                    conn.getOutputStream());
            // 构建post数据
            sb.setLength(0);
//            for (ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()) {
//                final String key = entry.getKey();
//                final String value = entry.getValue();
//                sb.append(key);
//                sb.append("=");
//                sb.append(value);
//                sb.append("&");
//            }
            sb.append("username=haha&password=xixi");
            Log.e(TAG, sb.toString());
            dos.write(sb.toString().getBytes());
            // 刷新输出流
            dos.flush();
            dos.close();
            // 获取返回数据
            InputStream is = conn.getInputStream();

            sb.setLength(0);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            conn.disconnect();
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }
}
