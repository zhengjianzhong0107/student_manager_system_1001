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
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_class")
@ToString
public class DlClassEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 班号
	 */
	@TableField("class_no")
	private String classNo;

	/**
	 * 班主任ID
	 */
	@TableField("master_id")
	private String masterId;

	/**
	 * 班级人数
	 */
	@TableField("stu_num")
	private Integer stuNum;

	/**
	 * 班级教室ID
	 */
	@TableField("room_id")
	private String roomId;

	/**
	 * 状态1正常  2毕业
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 1小学 2初中 3高中
	 */
	@TableField("class_type")
	private Integer classType;

	/**
	 * 年级
	 */
	@TableField("grade_id")
	private String gradeId;


	//年级编号（用于判断是第级年级）千位表示教育阶段，百位表示几年一更，十位表示第几年级，个位表示上下册
	@TableField("grade_num")
	private Integer gradeNum;


	@TableField(exist = false)
	private DlGradeEntity grade;



	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 
	 */
	@TableField(value = "create_id", fill = FieldFill.INSERT)
	private String createId;

	/**
	 * 
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;


}
