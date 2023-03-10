package com.company.project.common.utils;

public class GradeUtils {


    public static String getNameByGradeNum(Integer gradeNum){
        if(gradeNum == null){
            return "未知";
        }
        int q = gradeNum / 1000;//千位值  教育阶段
        int b = gradeNum / 10 % 10; //十位 几年级
        String name = "";
        switch (q){
            case 1:
                name = "小";
                break;
            case 2:
                name = "初";
                break;
            case 3:
                name = "高";
                break;
            default:
                name = "";
        }

        switch (b){
            case 1:
                name += "一、";
                break;
            case 2:
                name += "二、";
                break;
            case 3:
                name += "三、";
                break;
            case 4:
                name += "四、";
                break;
            case 5:
                name += "五、";
                break;
            case 6:
                name += "六、";
                break;
            default:
                name += "";
        }
        return name;
    }
}
