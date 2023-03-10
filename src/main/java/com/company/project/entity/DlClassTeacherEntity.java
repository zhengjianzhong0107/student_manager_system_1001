package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_class_teacher")
@ToString
public class DlClassTeacherEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 教师ID
	 */
	@TableField("teacher_id")
	private String teacherId;

	/**
	 * 课程ID
	 */
	@TableField("course_id")
	private String courseId;

	/**
	 * 班级ID
	 */
	@TableField("class_id")
	private String classId;

	/**
	 * 学年
	 */
	@TableField("term_id")
	private String termId;


}
