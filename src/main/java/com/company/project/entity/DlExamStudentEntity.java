package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-07 10:17:13
 */
@Data
@TableName("dl_exam_student")
public class DlExamStudentEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 
	 */
	@TableField("exam_plan_id")
	private String examPlanId;

	/**
	 * 
	 */
	@TableField("exam_list_id")
	private String examListId;

	/**
	 * 
	 */
	@TableField("exam_room_id")
	private String examRoomId;

	/**
	 * 
	 */
	@TableField("class_id")
	private String classId;

	/**
	 * 
	 */
	@TableField("student_id")
	private String studentId;

	/**
	 * 学生考号
	 */
	@TableField("stu_exam_num")
	private String stuExamNum;

	/**
	 * 座位号
	 */
	@TableField("stu_seat_num")
	private String stuSeatNum;

	/**
	 * 
	 */
	@TableField("stu_name")
	private String stuName;

	/**
	 * 
	 */
	@TableField("stu_num")
	private String stuNum;


}
