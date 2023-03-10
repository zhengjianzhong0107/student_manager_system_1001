package com.company.project.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Data
@TableName("dl_exam_plan")
@ToString
public class DlExamPlanEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 考试标题
	 */
	@TableField("name")
	private String name;

	/**
	 * 考试开始时间
	 */
	@TableField("time_start")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date timeStart;

	/**
	 * 考试描述
	 */
	@TableField("exam_desc")
	private String examDesc;

	/**
	 * 考试结束时间
	 */
	@TableField("time_end")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date timeEnd;

	/**
	 * 教考教师人数， 默认2个
	 */
	@TableField("invigilator_num")
	private Integer invigilatorNum;

	/**
	 * 考试计划状态 1.已保存 2.已发布 3.已完成
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 成绩录入状态 1允许录入  2不能录入
	 */
	@TableField("result_input_status")
    private Integer resultInputStatus;


	@TableField("class_type")
	private Integer classType;
	//学期ID
	@TableField("term_id")
	private String termId;
	//学期名称
	@TableField("term_name")
	private String termName;

	/**
	 * 参考年级
	 */
	@TableField("grade_id")
	private String gradeId;

	/**
	 * 考试类型(期末考试，期中考试，月考，周考）
	 */
	@TableField("exam_type")
	private Integer examType;

	@TableField(value = "create_id", fill = FieldFill.INSERT)
	private String createId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_id", fill = FieldFill.UPDATE)
	private String updateId;
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private Date updateTime;

	/**
	 * 
	 */
	@TableField(value = "deleted", fill = FieldFill.INSERT)
	private Integer deleted;

	@TableField(exist = false)
	private List<String> classIds;//班级列表ID
	@TableField(exist = false)
	private List<String> courseIds;//年级列表ID


}
