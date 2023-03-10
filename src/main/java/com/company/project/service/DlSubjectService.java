package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlSubjectEntity;

import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlSubjectService extends IService<DlSubjectEntity> {
    List<DlSubjectEntity> getListByClassType(Integer classType);
}

