package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrangingCourseEntity;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-19 15:22:38
 */
public interface DlArrangingCourseService extends IService<DlArrangingCourseEntity> {
    /**
     * 智能排课方法
     * @return
     */
    DataResult autoArrayCourse(String arrId);

}

