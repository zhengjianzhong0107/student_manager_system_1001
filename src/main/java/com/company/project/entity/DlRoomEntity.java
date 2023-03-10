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
@TableName("dl_room")
public class DlRoomEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 教室名称
	 */
	@TableField("room_name")
	private String roomName;

	/**
	 * 教室类型
	 */
	@TableField("room_type")
	private Integer roomType;

	/**
	 * 容纳量
	 */
	@TableField("capacity")
	private Integer capacity;

	/**
	 * 已有量
	 */
	@TableField("has_num")
	private Integer hasNum;

	/**
	 * 描述
	 */
	@TableField("room_desc")
	private String roomDesc;

	/**
	 * 地址
	 */
	@TableField("address")
	private String address;

	/**
	 * 状态 1.正常  0.停用  默认是1
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;


	@TableField(exist = false)
	private SysDictDetailEntity classRoomType;


}
