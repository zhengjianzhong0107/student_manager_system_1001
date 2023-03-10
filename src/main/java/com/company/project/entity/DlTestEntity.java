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
@TableName("dl_test")
public class DlTestEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 试题编号
	 */
	@TableField("code")
	private String code;

	/**
	 * 试卷名称
	 */
	@TableField("test_name")
	private String testName;

	/**
	 * 注意事项
	 */
	@TableField("notes")
	private String notes;

	/**
	 * 考试时长
	 */
	@TableField("duration")
	private Double duration;

	/**
	 * 试卷总分
	 */
	@TableField("score")
	private Double score;

	/**
	 * 
	 */
	@TableField("create_id")
	private String createId;

	/**
	 * 
	 */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 
	 */
	@TableField("update_id")
	private String updateId;

	/**
	 * 
	 */
	@TableField("update_time")
	private Date updateTime;

	/**
	 * 私有化1是2否
	 */
	@TableField("self")
	private Integer self;

	/**
	 * 1正常0删除
	 */
	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;

	/**
	 * 科目ID
	 */
	@TableField("subject_id")
	private String subjectId;

	/**
	 * 适用年级
	 */
	@TableField("grade_type")
	private Integer gradeType;


}
