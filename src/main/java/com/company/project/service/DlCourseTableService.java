package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlCourseTableEntity;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-02-07 10:45:06
 */
public interface DlCourseTableService extends IService<DlCourseTableEntity> {
    DataResult findAllowPosition(String id);
}

