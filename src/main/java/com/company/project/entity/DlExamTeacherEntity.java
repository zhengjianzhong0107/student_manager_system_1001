package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-28 13:09:20
 */
@Data
@TableName("dl_exam_teacher")
public class DlExamTeacherEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 考试计划ID
	 */
	@TableField("exam_id")
	private String examId;

	/**
	 * 教师ID
	 */
	@TableField("teacher_id")
	private String teacherId;

	/**
	 * 教师名称
	 */
	@TableField("teacher_name")
	private String teacherName;

	/**
	 * 工号
	 */
	@TableField("username")
	private String username;


}
