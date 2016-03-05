package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.magusutils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户类
 * Created by pau on 15/6/6.
 */
public class User implements Serializable {
    private static final String TAG = "User";
    private String userId = "";
    private String userPswd = "";
    private String description = "";
    private int isDeleted = 0;
    private int isSystem = 0;
    private String orgId = "";
    private String roleId = "";
    private String roleLevel = "";
    private String roleName = "";
    private String userFullName = "";
    private String userNo = "";
    private String userName="";
    private String userType="";
    private List<PagerInfo> pagerInfos= new ArrayList<PagerInfo>();
    public static boolean isLogin = false;//是否登入状态

    public User() {
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<PagerInfo> getPagerInfos() {
        return pagerInfos;
    }

    public void setPagerInfos(List<PagerInfo> pagerInfos) {
        this.pagerInfos = pagerInfos;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        User.isLogin = isLogin;
    }

    public String getUserPswd() {
        return userPswd;
    }

    public void setUserPswd(String userPswd) {
        this.userPswd = userPswd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(int isSystem) {
        this.isSystem = isSystem;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public static void parseUserInfo(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        JSONObject object = array.getJSONObject(0);
                        List<PagerInfo> list = new ArrayList<PagerInfo>();
                        if (object.has("rolePage")){
                            JSONArray array1 = new JSONArray(object.getString("rolePage"));
                            for (int i=0;i<array1.length();i++){
                                PagerInfo pagerInfo = new PagerInfo();
                                if (array1.getJSONObject(i).has("code")){
                                    pagerInfo.setCode(array1.getJSONObject(i).getString("code"));
                                }
                                if (array1.getJSONObject(i).has("url")){
                                    pagerInfo.setUrl(array1.getJSONObject(i).getString("url"));
                                }
                                list.add(pagerInfo);
                            }
                        }

                        if (object.has("user")){
                            User user  = new User();
                            JSONObject object1 = new JSONObject(object.getString("user"));
                            if (object1.has("description")){
                                user.setDescription(object1.getString("description"));
                            }
                            if (object1.has("isDeleted")){
                                user.setIsDeleted(Integer.parseInt(object1.getString("isDeleted")));
                            }
                            if (object1.has("isSystem")){
                                user.setIsSystem(Integer.parseInt(object1.getString("isSystem")));
                            }
                            if (object1.has("orgId")){
                                user.setOrgId(object1.getString("orgId"));
                            }
                            if (object1.has("roleId")){
                                user.setRoleId(object1.getString("roleId"));
                            }
                            if (object1.has("roleName")){
                                user.setRoleName(object1.getString("roleName"));
                            }
                            if (object1.has("userFullname")){
                                user.setUserFullName(object1.getString("userFullname"));
                            }
                            if (object1.has("userId")){
                                user.setUserId(object1.getString("userId"));
                            }
                            if (object1.has("userName")){
                                user.setUserName(object1.getString("userName"));
                            }
                            if (object1.has("userNo")){
                                user.setUserNo(object1.getString("userNo"));
                            }
                            if (object1.has("userType")){
                                user.setUserType(object1.getString("userType"));
                            }
                            user.setPagerInfos(list);
                            MyApplication.initUserInfo(user);
                            SharedPreferenceUtil.save("username",user.getUserFullName());
                            callBack.onSuccess(user);
                        }
                    }
                } else {
                    callBack.onFailed("请求失败");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPswd='" + userPswd + '\'' +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                ", isSystem=" + isSystem +
                ", orgId='" + orgId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleLevel='" + roleLevel + '\'' +
                ", roleName='" + roleName + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", userNo='" + userNo + '\'' +
                ", userName='" + userName + '\'' +
                ", pagerInfos=" + pagerInfos +
                '}';
    }
}
