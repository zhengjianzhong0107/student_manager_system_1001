package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlExamRoomEntity;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
public interface DlExamRoomService extends IService<DlExamRoomEntity> {
    /**
     * 通过考试计划ID查询现在的考场容量是否能够满足参考学生安置
     * @param examId
     * @return
     */
    boolean checkRoomIsEnough(String examId);

    DlExamRoomEntity getExamRoomBeanByStuId(String stuId, String planId);
}

