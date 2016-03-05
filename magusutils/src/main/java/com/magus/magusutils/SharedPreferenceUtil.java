package com.magus.magusutils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

/**
 * 共享数据存取类
 */
public class SharedPreferenceUtil {

    private static String CONFIG_FILE_NAME="ENVIROMENT";
    private static SharedPreferences pref;

    static {
        pref = ContextUtil.context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 判断是否存在该key值
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return pref.contains(key);
    }

    /**
     * 重设指定字段
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 共享数据中存储数据
     * @param key
     * @param value
     */
    public static void save(String key,String value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取数据
     * @param key
     * @param defVal
     * @return
     */
    public static String get(String key,String defVal){
        return pref.getString(key,defVal);
    }

    /**
     * 共享数据中存储数据
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取数据
     * @param key
     * @param defVal
     * @return
     */
    public static long getLong(String key, long defVal){
        return pref.getLong(key, defVal);
    }

    public static void saveSet(String key, Set<String> value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }
    public static Set<String> getSet(String key){
        return pref.getStringSet(key, null);
    }
    /**
     * 共享数据中存储数据
     * @param key
     * @param value
     */

    public static void saveInt(String key, int value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取数据
     * @param key
     * @param defVal
     * @return
     */
    public static int getInt(String key, int defVal){
        return pref.getInt(key, defVal);
    }

    /**
     * 共享数据中存储布尔型数据
     * @param key
     * @param value
     */
    public static void saveBoolean(String key,boolean value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取布尔型数据
     * @param key
     * @param defVal
     * @return
     */
    public static boolean getBoolean(String key,boolean defVal){
        return pref.getBoolean(key, defVal);
    }

    /**
     * 保存对象缓存
     *
     * @param context
     * @param o
     */
    public static void saveObject(String key,Context context, Object o) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            if (o == null)
                return;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences(CONFIG_FILE_NAME, Activity.MODE_PRIVATE);
            String newsListBase64 = new String(Base64.encode(baos.toByteArray(), 1));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 将编码后的字符串写到base64.xml文件中
            editor.putString(key,
                    newsListBase64);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    oos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                    baos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得对象缓存
     *
     * @param context
     * @return
     */
    public static Object getObject(String key,Context context) {
        Object object = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {

            long TIMESTART = System.currentTimeMillis();

            SharedPreferences sharedPreferences = context
                    .getSharedPreferences(CONFIG_FILE_NAME, Activity.MODE_PRIVATE);
            String newsListBase64 = sharedPreferences.getString(key, null);

            if (newsListBase64 == null) {
                return null;
            }
            // 对Base64格式的字符串进行解码
            byte[] base64Bytes = Base64.decode(newsListBase64.getBytes(), 1);
            bais = new ByteArrayInputStream(base64Bytes);
            ois = new ObjectInputStream(bais);
            object = ois.readObject();
        } catch (Exception e) {
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    ois = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                    bais = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }


}