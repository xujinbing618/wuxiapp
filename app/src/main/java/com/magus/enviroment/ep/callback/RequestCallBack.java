package com.magus.enviroment.ep.callback;

import java.util.List;

/**
 * Created by pau on 15/6/1.
 */
public abstract class RequestCallBack {

    public void onSuccess(List<?> list1,List<?> list2,List<?> list3,List<?> list4,List<?> list5,List<?> list6){};
    public void onSuccess(List<?> list){}
    public void onSuccess(List<?> list,int startRecord){}
    public void onSuccess(List<?> list,int startRecord,int totalRecord){}
    public void onSuccess(Object object){}
    public void onFailed(){}
    public void onFailed(String errorMessage){}

}
