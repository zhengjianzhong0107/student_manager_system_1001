package com.company.project.vo.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ToString
public class DlExamListVo {

    /**
     *
     */
    private String id;

    private String examName;

    /**
     * 考场ID
     */
    private String examRoom;

    private String classRoom;

    /**
     * 考试计划ID
     */
    private String examPlanId;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 任课教师ID
     */
    private String teacherName;

    /**
     * 考试日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date examTime;
    /**
     * 考试开始时间
     */
    @JSONField(format = "HH:mm:ss")
    private Date startTime;
    /**
     * 开始结束时间
     */
    @JSONField(format = "HH:mm:ss")
    private Date endTime;

    /**
     *
     */
    private Integer status;

}
