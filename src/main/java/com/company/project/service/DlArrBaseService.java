package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrBaseEntity;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-21 14:29:57
 */
public interface DlArrBaseService extends IService<DlArrBaseEntity> {
    DataResult copyClassTeacherToThisBean(String arrId);

    DataResult getTeacherList(DlArrBaseEntity bean);
    DataResult getCourseList(DlArrBaseEntity bean);

    DataResult getClassList(DlArrBaseEntity bean);
}

