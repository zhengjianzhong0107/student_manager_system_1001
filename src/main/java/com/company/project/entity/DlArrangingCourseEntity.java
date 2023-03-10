package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-19 15:22:38
 */
@Data
@TableName("dl_arranging_course")
@ToString
public class DlArrangingCourseEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 排课计划名称
	 */
	@TableField("arranging_name")
	private String arrangingName;

	/**
	 * 学期ID
	 */
	@TableField("term_id")
	private String termId;

	/**
	 * 周上课天数
	 */
	@TableField("week_day_num")
	private Integer weekDayNum;

	/**
	 * 上午课程数
	 */
	@TableField("am_num")
	private Integer amNum;

	/**
	 * 下午课程数
	 */
	@TableField("pm_num")
	private Integer pmNum;

	/**
	 * 晚上课程数
	 */
	@TableField("night_num")
	private Integer nightNum;

	/**
	 * 状态   1启用  0禁用
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 
	 */
	@TableField(value = "create_id", fill = FieldFill.INSERT)
	private String createId;

	/**
	 * 
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * 
	 */
	@TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
	private String updateId;

	/**
	 * 
	 */
	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;

	/**
	 * 参排年级ID
	 */
	@TableField(exist = false)
	private String[] gradeIds;
}
