package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlExamClassEntity;

/**
 * 
 * 
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlExamClassMapper extends BaseMapper<DlExamClassEntity> {
    /**
     * 通过ExamID查询该考试计划中参考学生的人数
     * @param examId
     * @return
     */
    Integer getExamStudentCount(String examId);
}
