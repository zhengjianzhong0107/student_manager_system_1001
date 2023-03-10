package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_exam_class")
public class DlExamClassEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 
	 */
	@TableField("exam_id")
	private String examId;

	/**
	 * 
	 */
	@TableField("class_id")
	private String classId;

	@TableField("class_name")
	private String className;

	/**
	 * 
	 */
	@TableField("remark")
	private String remark;


}
