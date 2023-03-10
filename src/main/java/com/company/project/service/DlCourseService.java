package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlCourseEntity;

import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlCourseService extends IService<DlCourseEntity> {
    List<DlCourseEntity> getListByGrade(String grade);
    List<DlCourseEntity> getListByClassType(Integer classType);
}

