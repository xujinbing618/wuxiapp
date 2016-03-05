package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlarmProblem {
    //id
    private String id;
    //类型ID
    private String typeid;
    //问题
    private String problem;
    //联系方式
    private String contact;
    //回复
    private String answer;
    //类型名称
    private String type_name;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String typeName) {
        type_name = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public static void parseInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("{}")) {
                        JSONObject jsonObject = new JSONObject(o.getString("resultEntity"));
                        int startRecord = 0;
                        if (jsonObject.has("startRecord")) {
                            startRecord = jsonObject.getInt("startRecord");
                        }
                        int totalRecord = 0;
                        if (jsonObject.has("totalRecord")) {
                            totalRecord = jsonObject.getInt("totalRecord");
                        }

                        if (jsonObject.has("data") && !jsonObject.getString("data").trim().equals("[]")) {
                            JSONArray array = new JSONArray(jsonObject.getString("data"));
                            List<AlarmProblem> list = new ArrayList<AlarmProblem>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmProblem typeInfo = new AlarmProblem();
                                if (object.has("id")) {
                                    typeInfo.setId(object.getString("id"));
                                }
                                if (object.has("typeid")) {
                                    typeInfo.setTypeid(object.getString("typeid"));
                                }
                                if (object.has("problem")) {
                                    typeInfo.setProblem(object.getString("problem"));
                                }
                                if (object.has("contact")) {
                                    typeInfo.setContact(object.getString("contact"));
                                }
                                if (object.has("answer")) {
                                    typeInfo.setAnswer(object.getString("answer"));
                                }
                                if (object.has("type_name")) {
                                    typeInfo.setType_name(object.getString("type_name"));
                                }

                                list.add(typeInfo);
                            }
                            callBack.onSuccess(list, startRecord, totalRecord);
                        } else {
                            callBack.onFailed();
                        }
                    } else {
                        callBack.onFailed();
                    }

                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }


}
