package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlExamSubjectEntity;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
public interface DlExamSubjectService extends IService<DlExamSubjectEntity> {
    /**
     * 检查是否设置完成
     * @param examId
     * @return
     */
    boolean checkSetIsComplete(String examId);

    /**
     * 检查考试时间上存在重叠的情况
     * @param bean
     * @return true:表示存在冲突重叠， false表示不存在
     */
    boolean timeIsOverlap(DlExamSubjectEntity bean);
}

