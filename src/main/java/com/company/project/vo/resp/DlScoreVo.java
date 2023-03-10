package com.company.project.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class DlScoreVo {
    private String classId;
    private String studentId;
    private String studentName;
    private String courseName;
    private String courseId;
    private BigDecimal score;
    private BigDecimal totalScore;
    private BigDecimal classScoreAvg;
    private BigDecimal gradeScoreAvg;
    private Integer classOrderNum;
    private Integer gradeOrderNum;

}
