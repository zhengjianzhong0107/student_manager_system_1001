package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_grade")
@ToString
public class DlGradeEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 年级名称
	 */
	@TableField("grade_name")
	private String gradeName;

	/**
	 * 类型：小学、初中、高中
	 */
	@TableField("type")
	private Integer type;

	//年级编号（用于判断是第级年级）千位表示几年一更，百位表示所属类型，十位表示第几年级，个位表示上下册
	@TableField("grade_num")
	private Integer gradeNum;

	/**
	 * 年级全称
	 */
	@TableField("grade_name_label")
	private String gradeNameLabel;

	/**
	 * 1正常  2毕业
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 
	 */
	@TableField("deleted")
	private Integer deleted;

	/**
	 * 年级下的所有班级信息
	 */
	@TableField(exist = false)
	private List<DlClassEntity> classList;


}
