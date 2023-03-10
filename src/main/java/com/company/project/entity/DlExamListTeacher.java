package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@TableName("dl_exam_list_teacher")
@ToString
public class DlExamListTeacher extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId("id")
    private String id;


    @TableField("teacher_id")
    private String teacherId;

    @TableField("exam_list_id")
    private String examListId;

    @TableField("teacher_name")
    private String teacherName;


    /**
     * 是否为主监考 1是 2否
     */
    @TableField("main")
    private Integer main;
}
