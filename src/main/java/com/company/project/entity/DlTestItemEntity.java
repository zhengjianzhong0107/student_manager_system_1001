package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_test_item")
public class DlTestItemEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 试卷ID
	 */
	@TableField("test_id")
	private String testId;

	/**
	 * 试题类型
	 */
	@TableField("question_type")
	private Integer questionType;

	/**
	 * 大题名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 大题描述
	 */
	@TableField("question_desc")
	private String questionDesc;

	/**
	 *
	 */
	@TableField("question_num")
	private Integer questionNum;

	/**
	 * 
	 */
	@TableField("score")
	private Double score;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	@TableField(exist = false)
	private List<DlTestDetailEntity> details;

}
