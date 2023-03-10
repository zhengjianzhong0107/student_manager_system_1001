package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_course")
public class DlCourseEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 课程名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 所属年级
	 */
	@TableField("grade")
	private Integer grade;

	/**
	 * 周课时数
	 */
	@TableField("week_num")
	private Integer weekNum;

	/**
	 * 学科ID
	 */
	@TableField("subject_id")
	private String subjectId;

	/**
	 * 课程类型（主课，副课，普通课）字典
	 */
	@TableField("course_type")
	private Integer courseType;

	/**
	 * 创建人
	 */
	@TableField(value = "create_id", fill = FieldFill.INSERT)
	private String createId;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 更新人
	 */
	@TableField(value = "update_id", fill = FieldFill.UPDATE)
	private String updateId;

	/**
	 * 更行时间
	 */
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private Date updateTime;

	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;

}
