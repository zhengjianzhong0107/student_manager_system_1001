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
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_subject")
public class DlSubjectEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 课程名称
	 */
	@TableField("course_name")
	private String courseName;

	/**
	 * 课程描述
	 */
	@TableField("course_desc")
	private String courseDesc;

	/**
	 * 学科所属类型（小中高）
	 */
	@TableField("class_type")
	private Integer classType;

	/**
	 * 
	 */
	@TableField("deleted")
	private Integer deleted;

	@TableField(exist = false)
	private SysDictDetailEntity classTypeBean;


}
