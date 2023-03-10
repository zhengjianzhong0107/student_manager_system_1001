package com.company.project.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ScoreRangeVO {
    public ScoreRangeVO(){

    }

    public ScoreRangeVO(String courseName){
        this.courseName = courseName;
    }
    private String resultId;
    private String courseId;
    private String courseName;
    private String classId;
    private String className;
    private Integer ninety = 0;//总分90%分以上的人数
    private Integer eighty = 0;//80%分以上的人数
    private Integer seventy = 0;//70%分以上的人数
    private Integer sixty = 0;//60%分以上的人数
    private Integer thirty = 0;//30%分以上的人数
    private Integer zero = 0;//0%分以上的人数
    private BigDecimal avg;//平均分
    private BigDecimal totalScore;
}
