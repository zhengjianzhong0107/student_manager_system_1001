package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamListEntity;
import com.company.project.entity.DlExamListTeacher;

import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlExamListService extends IService<DlExamListEntity> {
    /**
     * 智能快速排考
     * @param examId
     */
    void fastAdd(String examId);

    List<DlExamListEntity> getListByExamId(DlExamListEntity bean);

    List<DlExamListEntity> getExportListByExamId(String examId);

    /**
     * 删除相关的数据
     * @param examId
     */
    void removeListByExamId(String examId);

    /**
     * 获取自己的考务信息列表
     * @param bean
     * @return
     */
    DataResult getMyExamPlanList(DlExamListEntity bean);

    void removeListTeacherByTeacherId(String listTeacherId);

    /**
     * 通过列表ID查询旗下的所有监考教师列表
     * @param listId
     * @return
     */
    List<DlExamListTeacher> getListTeachersByListId(String listId);

    void saveTeacher(DlExamListTeacher bean);

    void updateTeacher(DlExamListTeacher bean);
}

