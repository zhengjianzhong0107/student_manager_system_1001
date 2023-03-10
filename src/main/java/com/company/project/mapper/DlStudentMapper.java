package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlStudentEntity;

/**
 * 
 * 
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlStudentMapper extends BaseMapper<DlStudentEntity> {
    Integer getClassTypeByStudentId(String studentId);
}
