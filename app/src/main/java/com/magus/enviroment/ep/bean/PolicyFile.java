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
 * 政策文件
 * Created by pau on 15/7/17.
 */
public class PolicyFile implements Serializable {

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

    public PolicyFile() {
    }

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

    public static void parsePolicyFile(String response, RequestCallBack callBack) {
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
                        List<PolicyFile> list = new ArrayList<PolicyFile>();
                        if (object2.has("data")) {
                            JSONArray array = new JSONArray(object2.getString("data"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                PolicyFile policyFile = new PolicyFile();
                                if (object.has("fileUrl")) {
                                    policyFile.setFileUrl(URLConstant.HEAD_URL+URLConstant.FILE_NAME+object.getString("fileUrl"));
                                }
                                if (object.has("fileName")) {
                                    policyFile.setFileName(object.getString("fileName"));
                                }
                                list.add(policyFile);
                            }
                            callBack.onSuccess(list,start);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed("请求失败");
        }
    }

    @Override
    public String toString() {
        return "PolicyFile{" +
                "fileBelongStation='" + fileBelongStation + '\'' +
                ", fileCreateTime='" + fileCreateTime + '\'' +
                ", fileDownloadCount='" + fileDownloadCount + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileTypeName='" + fileTypeName + '\'' +
                ", fileUpdateTime='" + fileUpdateTime + '\'' +
                ", fileUploadStation='" + fileUploadStation + '\'' +
                ", fileUploadTime='" + fileUploadTime + '\'' +
                ", fileUploadUser='" + fileUploadUser + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", remark='" + remark + '\'' +
                ", stationName='" + stationName + '\'' +
                ", userFullName='" + userFullName + '\'' +
                '}';
    }
}
