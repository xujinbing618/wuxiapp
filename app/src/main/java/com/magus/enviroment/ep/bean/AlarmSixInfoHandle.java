package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class AlarmSixInfoHandle {
    private String dictionary_id;//异常id
    private String dic_name;//异常名称

    public String getDictionary_id() {
        return dictionary_id;
    }

    public void setDictionary_id(String dictionary_id) {
        this.dictionary_id = dictionary_id;
    }

    public String getDic_name() {
        return dic_name;
    }

    public void setDic_name(String dic_name) {
        this.dic_name = dic_name;
    }

    public static void parseInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("[]")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AlarmSixInfoHandle> list = new ArrayList<AlarmSixInfoHandle>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AlarmSixInfoHandle typeInfo = new AlarmSixInfoHandle();
                            if (object.has("dictionary_id")) {
                                typeInfo.setDictionary_id(object.getString("dictionary_id"));
                            }
                            if (object.has("dic_name")) {
                                typeInfo.setDic_name(object.getString("dic_name"));
                            }
                            list.add(typeInfo);
                        }
                        callBack.onSuccess(list);
                    }
                } else {
                    callBack.onFailed();
                }

            }

        } catch (Exception e) {
            callBack.onFailed();
        }
    }

}
