package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
@Data
@TableName("dl_exam_result")
public class DlExamResultEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 
	 */
	@TableField("exam_id")
	private String examId;

	/**
	 * 
	 */
	@TableField("exam_title")
	private String examTitle;



	/**
	 * 
	 */
	@TableField("class_id")
	private String classId;

	@TableField("class_name")
	private String className;

	/**
	 * 
	 */
	@TableField("course_id")
	private String courseId;

	/**
	 * 课程名称
	 */
	@TableField("course_name")
	private String courseName;



	/**
	 * 任课教师ID
	 */
	@TableField("teacher_id")
	private String teacherId;

	/**
	 * 状态  1未完成  2已完成
	 */
	@TableField("status")
	private Integer status;

	@TableField("grade_id")
	private String gradeId;

	/**
	 * 成绩录入状态 1.表示允许录入， 2表示不允许录入
	 */
	@TableField("input_status")
	private Integer inputStatus;

	/**
	 * 考试日期
	 */
	@TableField("exam_time")
	private Date examTime;


}
