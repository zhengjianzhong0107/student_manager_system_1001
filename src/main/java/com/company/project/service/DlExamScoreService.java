package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlExamScoreEntity;
import com.company.project.vo.resp.DlStudentScoreVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-07 17:27:40
 */
public interface DlExamScoreService extends IService<DlExamScoreEntity> {
    /**
     * 通过学生ID查询该学生的历史考试成绩
     * @param studentId
     * @return
     */
    List<DlStudentScoreVo> getStuScoreListByStuId(String studentId);

    Map<String, Object> getStudentDetailsByStuId(String studentId);

    Map<String, Object> getReportByExamIdAndStudentId(DlExamScoreEntity bean);

    /**
     * 查询年级成绩报表数据
     * @param examId
     * @return
     */
    Map<String, Object> getGradeReportDataByExamId(String examId);

    /**
     * 查询班级成绩报表数据
     * @param examId
     * @param classId
     * @return
     */
    Map<String, Object> getClassReportDataByExamIdAndClassId(String examId, String classId);
}

