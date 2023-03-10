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
 * @date 2021-02-07 10:45:06
 */
@Data
@TableName("dl_course_table")
public class DlCourseTableEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 第几节课
	 */
	@TableField("row")
	private Integer row;

	/**
	 * 周几
	 */
	@TableField("col")
	private Integer col;

	/**
	 * 
	 */
	@TableField("base_id")
	private String baseId;

	/**
	 * 
	 */
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
	@TableField("class_id")
	private String classId;

	/**
	 * 
	 */
	@TableField("course_id")
	private String courseId;

	@TableField("room_id")
	private String roomId;

	@TableField("room_name")
	private String roomName;

	/**
	 * 
	 */
	@TableField("teacher_name")
	private String teacherName;

	/**
	 * 
	 */
	@TableField("class_name")
	private String className;

	/**
	 * 
	 */
	@TableField("course_name")
	private String courseName;

	/**
	 * 第几周
	 */
	@TableField("week")
	private Integer week;

	/**
	 * 学期ID
	 */
	@TableField("term_id")
	private String termId;

	@TableField(exist = false)
	private Integer level;


}
