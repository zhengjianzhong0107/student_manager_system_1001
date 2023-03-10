package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlExamTeacherEntity;

import java.util.List;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-28 13:09:20
 */
public interface DlExamTeacherService extends IService<DlExamTeacherEntity> {
    /**
     * 快速添加监考教师
     * @param examId
     */
    void fastAddTeachers(String examId);
    List<DlExamTeacherEntity> getListByExamId(String examId);
}

