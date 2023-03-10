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
 * @date 2020-12-04 18:03:46
 */
@Data
@TableName("dl_exam_room")
public class DlExamRoomEntity extends BaseEntity implements Serializable {
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
	@TableField("room_id")
	private String roomId;

	/**
	 * 考场名称
	 */
	@TableField("room_name")
	private String roomName;

	/**
	 * 教室名称
	 */
	@TableField("class_room_name")
	private String classRoomName;

	/**
	 * 容纳量
	 */
	@TableField("capacity")
	private Integer capacity;

	/**
	 * 主监考ID
	 */
	@TableField("chief_examiner")
	private String chiefExaminer;

	/**
	 * 副监考ID
	 */
	@TableField("deputy_examiner")
	private String deputyExaminer;

	/**
	 * 副监考ID
	 */
	@TableField("deputy_examiner2")
	private String deputyExaminer2;


}
