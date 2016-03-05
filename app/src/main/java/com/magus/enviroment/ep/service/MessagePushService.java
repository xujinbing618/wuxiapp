package com.magus.enviroment.ep.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.activity.my.MyMessageActivity;
import com.magus.enviroment.ep.bean.PushBean;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.magusutils.DateUtil;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pau on 15/7/7.
 */
public class MessagePushService extends Service {
    private static final String TAG = "MessagePushService";
    //获取消息线程
    private MessageThread messageThread = null;
    public boolean isRunning = true;
    private String time = "10";
    //点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;
    //通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null;
    private NotificationManager messageNotificationManager = null;
    private ScheduledExecutorService scheduledExecutorService;

    private String message = "";

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("开启服务");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent==null){
            System.out.println("intent为空");
            time= SharedPreferenceUtil.get("Time", "10");
        }else{
            time = intent.getStringExtra("time");
        }
        System.out.println("onStartCommand"+time);
        //初始化
        messageNotification = new Notification();
        messageNotification.icon = R.mipmap.logo;  //通知图片
        messageNotification.tickerText = "新消息";         //通知标题
        messageNotification.defaults = Notification.DEFAULT_SOUND;
        messageNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        messageNotification.flags = Notification.FLAG_AUTO_CANCEL;//点击消失
        //点击查看
        messageIntent = new Intent(this, MyMessageActivity.class);
        messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);
        //开启线程
        scheduled();
        return START_STICKY;
    }

    /**
     * 从服务端获取消息
     */
    class MessageThread implements Runnable {
        @Override
        public void run() {
            if (isRunning) {
                try {
                    //设置消息内容和标题
                    sendRequest();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 模拟了服务端的消息。实际应用中应该去服务器拿到message
     *
     * @return
     */
    public String getServerMessage() {
        return "yes";
    }

    public void scheduled() {

        //用一个定时器  来完成图片切换
        //Timer 与 ScheduledExecutorService 实现定时器的效果
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //经过指定的时间后，执行所指定的任务
        //scheduleAtFixedRate(command, initialDelay, period, unit)
        //command 所要执行的任务
        //initialDelay 第一次启动时 延迟启动时间
        //period  每间隔一小时来重新启动任务
        //unit 时间单位
        scheduledExecutorService.scheduleWithFixedDelay(new MessageThread(), 0, Long.parseLong(time), TimeUnit.MINUTES);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduledExecutorService.shutdownNow();
        isRunning = false;
        System.out.println("停止服务");
    }

    /**
     * 请求推送数据
     */
    private void sendRequest() {

        String url = URLConstant.HEAD_URL+URLConstant.URL_MY_PUSHMESSAGES + "?userId=" + MyApplication.mUid + "&minute=" +  time;
        System.out.println("请求推送"+time);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
       // request.setRetryPolicy(new DefaultRetryPolicy(10 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);

    }

    private void parseResponse(String response) {

        PushBean.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);

                List<PushBean> pushBeans = (List<PushBean>) list;
                //messageNotification.setLatestEventInfo(MessagePushService.this, DateUtil.getCurrentSecond() + "消息!", "您有"+list.size()+"条新消息" + messageNotificationID, messagePendingIntent);
                //发布消息
                messageNotificationManager.notify(messageNotificationID, messageNotification);
                //避免覆盖消息，采取ID自增
                messageNotificationID++;
//                for (int i=0;i<pushBeans.size();i++){
                // message=message+pushBeans.get(i).getAlarm_type_name()+pushBeans.get(i).getAlarm_detal()+"\t";
//                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, this);
    }
}
