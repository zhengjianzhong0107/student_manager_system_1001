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
 * @date 2021-02-21 12:50:04
 */
@Data
@TableName("dl_arr_grade")
public class DlArrGradeEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 
	 */
	@TableField("arr_id")
	private String arrId;


	/**
	 * 
	 */
	@TableField("grade_id")
	private String gradeId;

	/**
	 * 
	 */
	@TableField("grade_name")
	private String gradeName;

	/**
	 * 学期
	 */
	@TableField("term_id")
	private String termId;

	/**
	 * 状态： 1表示被选中 0表示未被选中 -1表示禁用--->CheckBoxStatusEnum
	 */
	@TableField(exist = false)
	private Integer status;

}
