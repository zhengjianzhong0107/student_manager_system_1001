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
@TableName("dl_student")
public class DlStudentEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId("id")
	private String id;

	/**
	 * 学号
	 */
	@TableField("s_no")
	private String sNo;

	/**
	 * 学生姓名
	 */
	@TableField("s_name")
	private String sName;

	/**
	 * 学籍号
	 */
	@TableField("ss_no")
	private String ssNo;

	/**
	 * 身份证号
	 */
	@TableField("id_card")
	private String idCard;

	/**
	 * 班级ID
	 */
	@TableField("class_id")
	private String classId;

	/**
	 * 母亲姓名
	 */
	@TableField("mather_name")
	private String matherName;

	/**
	 * 母亲电话
	 */
	@TableField("mather_phone")
	private String matherPhone;

	/**
	 * 年龄
	 */
	@TableField("age")
	private Integer age;

	/**
	 * 性别（1男，2女）
	 */
	@TableField("gender")
	private Integer gender;

	/**
	 * 联系电话
	 */
	@TableField("phone_num")
	private String phoneNum;

	/**
	 * 年级（19级等）
	 */
	@TableField("grade_id")
	private String gradeId;

	/**
	 * 父亲联系方式
	 */
	@TableField("father_phone")
	private String fatherPhone;

	/**
	 * 父亲姓名
	 */
	@TableField("father_name")
	private String fatherName;

	/**
	 * 入学类型
	 */
	@TableField("join_type")
	private Integer joinType;

	/**
	 * 入学时间
	 */
	@TableField("join_time")
	private Date joinTime;

	/**
	 * 状态（1.在校，2转校，3毕业）
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT, value = "create_time")
	private Date createTime;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT, value = "create_id")
	private String createId;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE, value = "update_time")
	private Date updateTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.UPDATE, value = "update_id")
	private String updateId;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 户籍地
	 */
	@TableField("address")
	private String address;

	/**
	 * 民族
	 */
	@TableField("nation")
	private String nation;

	/**
	 * 籍贯
	 */
	@TableField("native_place")
	private String nativePlace;

	/**
	 * 生日
	 */
	@TableField("birthday")
	private Date birthday;
	/**
	 * 现居住地址
	 */
	@TableField("now_address")
	private String nowAddress;
	/**
	 * 家庭住址
	 */
	@TableField("home_address")
	private String homeAddress;
	/**
	 * 健康状态
	 */
	@TableField("healthy")
	private String healthy;
	/**
	 * 联系地址
	 */
	@TableField("contact_address")
	private String contactAddress;

	/**
	 * 户口类型
	 */
	@TableField("native_type")
	private Integer nativeType;

	/**
	 * 就读方式
	 */
	@TableField("study_method")
	private String studyMethod;

	/**
	 * 入学方式
	 */
	@TableField("join_mode")
	private Integer joinMode;

	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;

	@TableField(exist = false)
	private Integer seatNum;

}
