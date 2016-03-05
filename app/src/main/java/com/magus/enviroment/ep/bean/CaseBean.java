package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/31.
 */
public class CaseBean implements Serializable {
    private String fileBelongStation = "";
    private String fileCreateTime = "";
    private String fileDownloadCount = "";
    private String fileId = "";
    private String fileName = "";
    private String fileType = "";
    private String fileTypeName = "";
    private String fileUpdateTime = "";
    private String fileUploadStation = "";
    private String fileUploadTime = "";
    private String fileUploadUser = "";
    private String fileUrl = "";
    private String isDelete = "";
    private String remark = "";
    private String stationName = "";
    private String userFullName = "";

    public String getFileBelongStation() {
        return fileBelongStation;
    }

    public void setFileBelongStation(String fileBelongStation) {
        this.fileBelongStation = fileBelongStation;
    }

    public String getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public String getFileDownloadCount() {
        return fileDownloadCount;
    }

    public void setFileDownloadCount(String fileDownloadCount) {
        this.fileDownloadCount = fileDownloadCount;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public String getFileUpdateTime() {
        return fileUpdateTime;
    }

    public void setFileUpdateTime(String fileUpdateTime) {
        this.fileUpdateTime = fileUpdateTime;
    }

    public String getFileUploadStation() {
        return fileUploadStation;
    }

    public void setFileUploadStation(String fileUploadStation) {
        this.fileUploadStation = fileUploadStation;
    }

    public String getFileUploadTime() {
        return fileUploadTime;
    }

    public void setFileUploadTime(String fileUploadTime) {
        this.fileUploadTime = fileUploadTime;
    }

    public String getFileUploadUser() {
        return fileUploadUser;
    }

    public void setFileUploadUser(String fileUploadUser) {
        this.fileUploadUser = fileUploadUser;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public static void parseCaseBean(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONObject object2 = new JSONObject(o.getString("resultEntity"));
                        int start =0;
                        if (object2.has("startRecord")){
                            start = object2.getInt("startRecord");
                        }
                        List<CaseBean> list = new ArrayList<CaseBean>();
                        if (object2.has("data")) {
                            JSONArray array = new JSONArray(object2.getString("data"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                CaseBean policyFile = new CaseBean();
                                if (object.has("fileUrl")) {
                                    policyFile.setFileUrl(URLConstant.HEAD_URL+URLConstant.FILE_NAME+object.getString("fileUrl"));
                                }
                                if (object.has("fileName")) {
                                    policyFile.setFileName(object.getString("fileName"));
                                }
                                if(object.has("remark")){
                                    policyFile.setRemark(object.getString("remark"));
                                }
                                list.add(policyFile);
                            }
                            callBack.onSuccess(list,start);
                        }
                    }
                }else {
                    callBack.onFailed();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed();
        }
    }

}
