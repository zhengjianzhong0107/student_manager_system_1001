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
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-06 14:13:43
 */
@Data
@TableName("dl_term")
public class DlTermEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 学年
	 */
	@TableField("school_year")
	private String schoolYear;

	/**
	 * 学期（1第一学期 2第二学期）
	 */
	@TableField("term")
	private Integer term;

	/**
	 * 开始时间
	 */
	@TableField("start_time")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * 状态 1启用 2禁用
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 
	 */
	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;


}
