package com.magus.enviroment.upload;

import android.util.Log;

import com.magus.enviroment.ep.callback.RequestCallBack;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 *
 * Author huarizhong
 * Date 2015/3/24 16:23
 * PackageName demo.hpg.org.pauldemo.upload
 */
public class FileUploadRequest implements Runnable {
    private static final String TAG="uploadFile";
    private static final int TIME_OUT=10*10000;
    private static final String CHARSET="utf-8";
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";
    /**
     * 上传地址
     */
    private String mFileUploadUrl;
    /**
     * 要上传的文件
     */
    private File mFile;

    /**
     *
     * @param file 上传的文件
     * @param fileUploadUrl 上传地址
     * @param callBack 回调
     */
    public FileUploadRequest(File file, String fileUploadUrl,RequestCallBack callBack){
        this.mFileUploadUrl = fileUploadUrl;
        this.mFile = file;
    }
    @Override
    public void run() {
        String BOUNDARY= UUID.randomUUID().toString();//边界标识
        String PREFIX="--";
        String LINE_END="\r\n";
        String CONTENT_TYPE="multipart/form-data";
        Log.e(TAG,mFile.length()+"---->");
        try {
            URL url = new URL(mFileUploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);

            if (mFile!=null){
                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + mFile.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                InputStream is = new FileInputStream(mFile);
                byte[] bytes = new byte[1024];
                int len=0;
                while((len=is.read(bytes))!=-1){
                    dos.write(bytes,0,len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_date=(PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_date);
                dos.flush();
                if (conn.getResponseCode()==200){
                    Log.e(TAG,"返回成功");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG,"失败");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "失败");
        }

    }
}
