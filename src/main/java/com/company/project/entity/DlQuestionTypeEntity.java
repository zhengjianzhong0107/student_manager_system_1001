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
 * @date 2020-12-18 10:21:28
 */
@Data
@TableName("dl_question_type")
public class DlQuestionTypeEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 类型名称
	 */
	@TableField("type_name")
	private String typeName;

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

	/**
	 * 
	 */
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private Date updateTime;

	/**
	 * 
	 */
	@TableField(value = "update_id", fill = FieldFill.UPDATE)
	private String updateId;

	/**
	 * 
	 */
	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;


}
