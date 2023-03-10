package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlClassEntity;

import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlClassService extends IService<DlClassEntity> {

    void updateRedisClassData();
    List<DlClassEntity> getClassDetailsList();
    List<DlClassEntity> getList();
    DlClassEntity getBeanByStr(String className);
    List<DlClassEntity> getGradeStudentNumAndGradeName();
}

