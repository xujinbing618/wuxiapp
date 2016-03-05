package com.magus.enviroment.ep.constant;

import com.magus.magusutils.SharedPreferenceUtil;

/**
 * 后台数据url
 * Created by pau on 15/5/25.
 */
public class URLConstant {
    /**
     * url头
     */
    public static String FILE_NAME = "/ep4.1_phoneapp";//erc
    public static String HEAD_URL = SharedPreferenceUtil.get("head_url", "http://103.44.145.243:10782");
    /**
     * 请求空气质量URLEncoder.encode("'呼和浩特'", "utf-8");
     */
    public static final String URL_AIR_STATION_INFO = FILE_NAME + "/Android/AlarmService/getAqiBycityName";

    /**----------------------------------------------------我的关注设置---------------------------------------------------------------------------**/
    /**
     * 请求关注的区域列表?userId=&roleId=
     */
    public static final String URL_ATTENTION_ZONE = FILE_NAME + "/Android/AttentionService/getPhoneUserAttentionZoneByUserId";
    /**
     * 请求关注的企业列表 userId=&roleId=&zoneId=
     */
    public static final String URL_ATTENTION_ENTERPRISE = FILE_NAME + "/Android/AttentionService/getPhoneUserAttentionPollByUserIdAndZoneId";
    /**
     * 请求报警异常类型 参数?userId=18043&&roleId=445
     */
    public static final String URL_ALARM_TYPE = FILE_NAME + "/Android/AlarmService/getFunctionRollMsg";
    /**
     * 污染源工厂
     */
    public static final String URL_ALARM_SOURCE = FILE_NAME + "/Android/AlarmService/getRelationCity";
    /**
     * 处理方式列表
     */
    public static final String URL_ALARM_HANDLE = FILE_NAME + "/Android/AlarmService/getAlarmSixCondition";
    /**
     * 提交关注的企业
     */
    public static final String URL_SUBMIT_ATTENTION = FILE_NAME + "/Android/AttentionService/attentionPollManager";
    /**
     * 提交关注异常类型 post
     */
    public static final String URL_SUBMIT_ALARM_TYPE = FILE_NAME + "/Android/AlarmService/updateCheckedRoll";
    /**
     * 提交六类报警异常处理
     */
    public static final String URL_SUBMIT_ALARM_SIX = FILE_NAME + "/Android/AlarmService/updateAlarmSixWarningMessageByUserId";
    /**------------------------------------------------------我的-------------------------------------------------------------------------**/
    /**
     * 获取我的消息详情
     */
    public static final String URL_MY_MESSAGE_INFO = FILE_NAME + "/Android/AlarmService/getAlarmPushMessageBySid";
    /**
     * 修改我的消息状态
     */
    public static final String URL_MY_MESSAGE_UPDATE = FILE_NAME + "/Android/AlarmService/updateAlarmRecordInfoVoRead";

    /**------------------------------------------------------异常详情-------------------------------------------------------------------------**/
    /**
     * 请求自定义报警列表数据?userId=4869
     */
    public static final String URL_ALARM_PIC_LIST = FILE_NAME + "/Android/AlarmService/getCustomAlarmApp";
    /**
     * 请求六类报警列表
     */

    public static final String URL_ALARM_DETAIL_LIST = FILE_NAME + "/Android/AlarmService/getAlarmSixWarningListByUserId";
    /**
     * 六类报警详请列表
     */
    public static final String URL_ALARM_SIX_INFO_LIST = FILE_NAME + "/Android/AlarmService/getAlarmSixWarningMessageByUserId";
    /**
     * 请求污染源列表
     */
    public static final String URL_ALARM_SOURCEVALUE = FILE_NAME + "/Android/AlarmService/getPollutantValue";
    /**
     * 异常详情内容?facilityBasId=472&&alarmCode=12&&start=0&&alarmTime=2015-05-29
     */
    public static final String URL_ALARM_DETAIL_CONTENT = FILE_NAME + "/Android/AlarmService/getAlarmInfoChildrenDetailListByAttention";
    /**
     * 登入
     */
    public static final String URL_LOGIN = FILE_NAME + "/Android/LoginService/login";
    /**
     * 提交异常处理
     */
    public static final String URL_SUBMIT_DEAL_ALARM = FILE_NAME + "/Android/AlarmService/alarmDealLogManager";
    /**
     * 异常原因userType=Operation&&alarmCode=7
     */
    public static final String URL_ALARM_COURSE = FILE_NAME + "/Android/AlarmService/getAlarmInfoCheckBoxList";
    /**
     * 异常统计柱状图
     */
    public static final String URL_ENTERPRISE_RATE = FILE_NAME + "/Android/AlarmService/getAlarmGridPercentage";
    /**
     * 请求异常统计折线图数据
     */
    public static final String URL_ZONE_RATE = FILE_NAME + "/Android/AlarmService/getAlarmCityPercentByMonth";
    /**
     * 工况异常报告列表userId=18043&&start=0
     */
    public static final String URL_ABNORMAL_REPORT = FILE_NAME + "/Android/AlarmService/getAlarmReportTypeA";
    /**
     * 工况异常报告详情地址？alarmLogId=1003602
     */
    public static final String URL_ATTENTION_ABNORMAL_DETAIL = FILE_NAME + "/Android/AlarmService/getAlarmReportTypeADetail";
    /**
     * 超标预警地址
     */
    public static final String URL_ATTENTION_OVERPRE_DETAIL = FILE_NAME + "/Android/AlarmService/getOverStandardPreAlarm";
    /**
     * 总量预警地址
     */
    public static final String URL_ATTENTION_ALARM_TOTAL = FILE_NAME + "/Android/AlarmService/getTotalAlarmApp";
    /**
     * 根据网格id获取数据
     */
    public static final String URL_ATTENTION_ALARM_GRID = FILE_NAME + "/Android/AlarmService/getGridAlarmPercent";//"/Android/AlarmService/getGridAlarm";
    /**
     * 网格考核地址
     */
    public static final String URL_ATTENTION_ALARM_GRID_ITEM = FILE_NAME + "/Android/AlarmService/getGridMessageByUserId";
    /**------------------------------------------------------异常详情-------------------------------------------------------------------------**/
    /**
     * ------------------------------------------------------超标报告-----------------------------------------------------------------------------*
     */
    /**
     * 请求超标报告列表
     */
    public static final String URL_REPORT_OVERALRAM_CONTENT = FILE_NAME + "/Android/AlarmService/getExcessiveReportingByUserId";
    /**
     * 超标报告详情（废弃）
     */
    public static final String URL_REPORT_OVERALRAM_CONTENT_ITEM = FILE_NAME + "/Android/AlarmService/getExcessiveReportingMessageByUserId";
    /**------------------------------------------------------报告-----------------------------------------------------------------------------**/
    /**
     * 留言提交
     */
    public static final String URL_SAVE_PROBLEM_MESSAGE = FILE_NAME + "/Android/AlarmService/saveProblemMessage";
    /**
     * 获取留言问题类型
     */
    public static final String URL_GET_PROBLEM_TYPE = FILE_NAME + "/Android/AlarmService/getAlarmProblemMessageType";
    /**
     * 常见问题列表
     */
    public static final String URL_FAQ_LIST = FILE_NAME + "/Android/AlarmService/getAlarmProblemMessage";
    /**
     * 检查更新
     */
    public static final String URL_CHECK_FOR_UPDATE = FILE_NAME + "/Android/VersionUpdateService/updateLocalVersion";
    /**
     * 政策文件列表
     * ?start=0
     */
    public static final String URL_POLICY_FILE = FILE_NAME + "/Android/SourceFileService/getSourceFileList";
    /**
     * 执法案例列表
     */
    public static final String URL_CASE_FILE = FILE_NAME + "/Android/SourceFileService/getSourceCaseFileList";
    /**
     * 总量监控折线图
     * ?pollSourceId=82&&pollutantCode=7
     */
    public static final String URL_MONITOR_POLLUTION_DATA = FILE_NAME + "/Android/PreAlarmService/getDayTotalEmissionForChart";
    /**
     * 请求年度排放总量
     * ?pollSourceId=82&pollutantCode=7
     */
    public static final String URL_MONITOR_POLLUTION_COUNT = FILE_NAME + "/Android/AlarmService/getAlarmPollTotal";

    /**
     * 请求推送数据
     */
    public static final String URL_MY_PUSHMESSAGES = FILE_NAME + "/Android/AlarmService/getAlarmPushMessageByUserId";
}
