package com.company.project.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_exam_list")
public class DlExamListEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 考场ID
	 */
	@TableField("exam_room_id")
	private String examRoomId;

	/**
	 * 考试计划ID
	 */
	@TableField("exam_plan_id")
	private String examPlanId;

	/**
	 * 课程ID
	 */
	@TableField("course_id")
	private String courseId;

	/**
	 * 任课教师ID
	 */
	@TableField("teacher_id")
	private String teacherId;

	/**
	 * 考试日期
	 */
	@TableField("exam_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date examTime;
	/**
	 * 考试开始时间
	 */
	@TableField("start_time")
	@JSONField(format="HH:mm:ss")
	private Date startTime;
	/**
	 * 开始结束时间
	 */
	@TableField("end_time")
	@JSONField(format="HH:mm:ss")
	private Date endTime;

	/**
	 * 
	 */
	@TableField(value = "status", fill = FieldFill.INSERT)
	private Integer status;

	private List<DlExamListTeacher> teachers;



}
