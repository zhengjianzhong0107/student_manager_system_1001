package com.company.project.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
@Data
@TableName("dl_exam_subject")
@ToString
public class DlExamSubjectEntity extends BaseEntity implements Serializable {
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
	@TableField("course_id")
	private String courseId;

	/**
	 * 
	 */
	@TableField("test_id")
	private String testId;

	/**
	 * 
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 考试日期
	 */
	@TableField("exam_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date examDate;

	/**
	 * 开考时间
	 */
	@TableField("start_time")
	@JSONField(format="HH:mm:ss")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@TableField("end_time")
	@JSONField(format="HH:mm:ss")
	private Date endTime;

	@TableField("course_name")
	private String courseName;

	/**
	 * 总分
	 */
	@TableField("total_score")
	private BigDecimal totalScore;


}
