package com.magus.enviroment.ep.util;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.magus.magusutils.ContextUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 下载管理
 */
public class MyDownloadManager {
    private static String TAG="MyDownloadManager";
    private DownloadManager downloadManager;
    private SharedPreferences prefs;
    private static final String DL_ID = "downloadIdsdfs";
    private static Context mContext;
    //pdf保存路径
    public static String mPath = Environment.getExternalStorageDirectory()
            + "/Environment/download/";
    private static MyDownloadManager instance;

    public MyDownloadManager() {
        this.mContext = ContextUtil.getContext();
        //创建文件夹
        File file = new File(mPath);
        if (!file.exists()){
            file.mkdirs();
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        downloadManager = (DownloadManager) mContext
                .getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public synchronized static MyDownloadManager getInstance() {
        if (instance == null) {
            instance = new MyDownloadManager();
        }
        return instance;
    }

    /**
     * 开始下载
     */
    public void download(String url) {

        String[] names = url.split("/");
        String fileName = names[names.length-1];
        Log.e(TAG,fileName);

        if (!prefs.contains(DL_ID)) {
            Uri resource = Uri.parse(encodeGB(url));
            DownloadManager.Request request = new DownloadManager.Request(
                    resource);
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
                    | Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            // 设置文件类型
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap
                    .getMimeTypeFromExtension(MimeTypeMap
                            .getFileExtensionFromUrl(url));
            request.setMimeType(mimeString);
            // // 在通知栏中隐藏,在小米miui中会出现安全异常
            // request.setNotificationVisibility(View.GONE);
            // request.setVisibleInDownloadsUi(false);
            // 在通知栏中显示
            request.setNotificationVisibility(View.VISIBLE);
            request.setVisibleInDownloadsUi(true);
            // sdcard的目录下的download文件夹
            request.setDestinationInExternalPublicDir("/Environment/download/", fileName);
            request.setTitle(names[names.length - 1]);
            long id = downloadManager.enqueue(request);
            // 保存id
            prefs.edit().putLong(DL_ID, id).commit();
        } else {
            // 下载已经开始，检查状态
            queryDownloadStatus(fileName);
        }
    }

    /**
     * 如果服务器不支持中文路径的情况下需要转换url的编码。
     */
    public String encodeGB(String string) {
        // 转换中文编码
        String split[] = string.split("/");
        for (int i = 1; i < split.length; i++) {
            try {
                split[i] = URLEncoder.encode(split[i], "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            split[0] = split[0] + "/" + split[i];
        }
        split[0] = split[0].replaceAll("\\+", "%20");// 处理空格
        return split[0];
    }

    public void queryDownloadStatus(String fileName) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(prefs.getLong(DL_ID, 0));
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c
                    .getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.e("HPG", "暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.e("HPG", "STATUS_PENDING");
                case DownloadManager.STATUS_RUNNING:
                    // 正在下载，不做任何事情
                    Log.e("HPG", "正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    // 完成
                    Log.e("HPG", "下载完成");
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/Environment/download/"+fileName);
                    openFile(file);
                    prefs.edit().clear().commit();
                    break;
                case DownloadManager.STATUS_FAILED:
                    // 清除已下载的内容，重新下载
                    Log.e("HPG", "下载失败");
                    downloadManager.remove(prefs.getLong(DL_ID, 0));
                    prefs.edit().clear().commit();
                    break;
            }
        }
    }

    /**
     * 取消下载
     */
    public void remove() {
        downloadManager.remove(prefs.getLong(DL_ID, 0));
        prefs.edit().clear().commit();
    }

    // 打开APK程序代码
    public void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e(TAG, file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}
