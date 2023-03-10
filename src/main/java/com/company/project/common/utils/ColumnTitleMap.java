package com.company.project.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColumnTitleMap {
    private Map<String, String> columnTitleMap = new HashMap<String, String>();
    private ArrayList<String> titleKeyList = new ArrayList<String> ();

    public ColumnTitleMap(String datatype) {
        switch (datatype) {
            case "userinfo":
                initUserInfoColu();
                initUserInfoTitleKeyList();
                break;
            default:
                break;
        }

    }
    /**
     * mysql用户表需要导出字段--显示名称对应集合
     */
    private void initUserInfoColu() {
        columnTitleMap.put("id", "ID");
        columnTitleMap.put("date_create", "注册时间");
        columnTitleMap.put("name", "名称");
        columnTitleMap.put("mobile", "手机号");
        columnTitleMap.put("email", "邮箱");
        columnTitleMap.put("pw", "密码");
        columnTitleMap.put("notice_voice", "语音通知开关");
        columnTitleMap.put("notice_email", "邮箱通知开关");
        columnTitleMap.put("notice_sms", "短信通知开关");
        columnTitleMap.put("notice_push", "应用通知开关");
    }

    /**
     * mysql用户表需要导出字段集
     */
    private void initUserInfoTitleKeyList() {
        titleKeyList.add("id");
        titleKeyList.add("date_create");
        titleKeyList.add("name");
        titleKeyList.add("mobile");
        titleKeyList.add("email");
        titleKeyList.add("pw");
        titleKeyList.add("notice_voice");
        titleKeyList.add("notice_email");
        titleKeyList.add("notice_sms");
        titleKeyList.add("notice_push");
    }

    public Map<String, String> getColumnTitleMap() {
        return columnTitleMap;
    }

    public ArrayList<String> getTitleKeyList() {
        return titleKeyList;
    }
}
