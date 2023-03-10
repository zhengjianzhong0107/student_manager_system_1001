package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_test_detail")
public class DlTestDetailEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 大题ID
	 */
	@TableField("item_id")
	private String itemId;

	/**
	 * 
	 */
	@TableField("content")
	private String content;

	/**
	 * 分数
	 */
	@TableField("score")
	private Double score;

	/**
	 * 试题ID
	 */
	@TableField("question_id")
	private String questionId;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 序号
	 */
	@TableField("order_num")
	private Integer orderNum;


}
