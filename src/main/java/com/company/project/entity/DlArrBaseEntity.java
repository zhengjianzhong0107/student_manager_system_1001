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
 * @date 2021-01-21 14:29:57
 */
@Data
@TableName("dl_arr_base")
public class DlArrBaseEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	@TableField("arr_id")
	private String arrId;

	/**
	 * 
	 */
	@TableField("teacher_id")
	private String teacherId;

	/**
	 * 
	 */
	@TableField("course_id")
	private String courseId;

	/**
	 * 
	 */
	@TableField("class_id")
	private String classId;

	/**
	 * 
	 */
	@TableField("room_id")
	private String roomId;

	/**
	 * 
	 */
	@TableField("teacher_name")
	private String teacherName;

	/**
	 * 
	 */
	@TableField("course_name")
	private String courseName;

	/**
	 * 
	 */
	@TableField("class_name")
	private String className;

	/**
	 * 
	 */
	@TableField("room_name")
	private String roomName;

	/**
	 * 周课时数
	 */
	@TableField("course_num")
	private Integer courseNum;

	/**
	 * 未排 课时数
	 */
	@TableField("sup_course_num")
	private Integer supCourseNum;

	/**
	 * 状态，1正常，2未排完，3排课完成
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 课程类型
	 */
	@TableField("course_type")
	private Integer courseType;


}
