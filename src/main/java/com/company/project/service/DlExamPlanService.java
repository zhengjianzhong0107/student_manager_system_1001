package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlExamPlanEntity;
import com.company.project.vo.resp.DLExamPlanVo;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlExamPlanService extends IService<DlExamPlanEntity> {
    /**
     * 检查监考教师人数是否满足监考任务
     * @param examId
     * @return
     */
    boolean checkTeacherIsEnough(String examId);

    /**
     * 创建考试学生信息
     * @param examId
     */
    void createExamStudentMessageResult(String examId);

    DLExamPlanVo getPlanVoBeanById(String id);
}

