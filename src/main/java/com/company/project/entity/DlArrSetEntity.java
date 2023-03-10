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
 * @date 2021-01-19 15:22:38
 */
@Data
@TableName("dl_arr_set")
public class DlArrSetEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 星期几
	 */
	@TableField("col")
	private Integer col;

	/**
	 * 第几节课
	 */
	@TableField("row")
	private Integer row;

	/**
	 * 类型，1教师不排课，2班级不排课，3课程不排课，4预排课
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 排课任务ID
	 */
	@TableField("arr_id")
	private String arrId;

	/**
	 * 学期
	 */
	@TableField("term_id")
	private String termId;

	/**
	 * 课程ID/教师ID/班级ID/班级课程ID
	 */
	@TableField("type_id")
	private String typeId;

	@TableField("label")
	private String label;


}
