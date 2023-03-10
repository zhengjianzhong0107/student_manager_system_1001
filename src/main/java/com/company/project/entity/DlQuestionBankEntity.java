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
@TableName("dl_question_bank")
@ToString
public class DlQuestionBankEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 试题类别
	 */
	@TableField("question_type")
	private String questionType;

	/**
	 * 科目
	 */
	@TableField("subject_id")
	private String subjectId;

	/**
	 * 内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 适用年级
	 */
	@TableField("grade_type")
	private Integer gradeType;

	/**
	 * 答案
	 */
	@TableField("answer")
	private String answer;

	/**
	 * 知识点ID
	 */
	@TableField("knowledge_id")
	private String knowledgeId;

	/**
	 * 创建人ID
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
	@TableField(value = "update_id", fill = FieldFill.UPDATE)
	private String updateId;

	/**
	 * 
	 */
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private Date updateTime;

	/**
	 * 
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 是否删除0是1否
	 */
	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;

	/**
	 * 是否私有1是2否
	 */
	@TableField("self")
	private Integer self;

	/**
	 * 难度（字典）
	 */
	@TableField("difficulty")
	private Integer difficulty;


}
