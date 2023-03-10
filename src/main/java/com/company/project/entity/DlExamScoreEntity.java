package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-07 17:27:40
 */
@Data
@TableName("dl_exam_score")
@ToString
public class DlExamScoreEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 学生ID
	 */
	@TableField("student_id")
	private String studentId;

	/**
	 * 学生姓名
	 */
	@TableField("student_name")
	private String studentName;

	/**
	 * 
	 */
	@TableField("score")
	private BigDecimal score;

	/**
	 * 
	 */
	@TableField("total_score")
	private BigDecimal totalScore;

	/**
	 * 
	 */
	@TableField("result_id")
	private String resultId;

	@TableField("remark")
	private String remark;

	@TableField("exam_id")
	private String examId;

	@TableField("course_id")
	private String courseId;

	@TableField("seat_num")
	private Integer seatNum;

	@TableField("exam_room_name")
	private String examRoomName;

	@TableField("exam_room_id")
	private String examRoomId;

	/**
	 * 用于数据查询时需要存数据的作用
	 */
	@TableField(exist = false)
	private String classId;
	@TableField(exist = false)
	private String courseName;
}
