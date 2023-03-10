package com.company.project.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class DlStudentScoreVo {
    private String examTitle;
    private String examId;
    private String studentId;
    private String studentName;
    private BigDecimal totalScore;
    private BigDecimal yw;
    private BigDecimal sx;
    private BigDecimal yy;
    private BigDecimal wl;
    private BigDecimal hx;
    private BigDecimal sw;
    private BigDecimal ls;
    private BigDecimal dl;
    private BigDecimal zz;
}
