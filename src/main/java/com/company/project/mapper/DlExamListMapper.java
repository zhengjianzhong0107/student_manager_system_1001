package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlExamListEntity;
import com.company.project.entity.DlExamListTeacher;
import com.company.project.vo.resp.DlExamListVo;

import java.util.List;

/**
 * 
 * 
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlExamListMapper extends BaseMapper<DlExamListEntity> {
    /**
     * 查询list
     * @param examPlanId
     * @return
     */
    List<DlExamListEntity> getListByExamId( String examPlanId);


	void removeTeachersByExamId(String examId);

    void saveTeacher(DlExamListTeacher teacher);

    void removeListByExamId(String examId);

    /**
     * 获取自己的考务信息列表
     * @param bean
     * @return
     */
    List<DlExamListVo> getMyExamPlanList(DlExamListEntity bean);

    /**
     * 通过列表教师ID删除列表中的教师信息
     * @param listTeacherId
     */
    void removeTeacherByTeacherId(String listTeacherId);

    /**
     * 通过列表ID查询旗下的所有监考教师列表
     * @param listId
     * @return
     */
    List<DlExamListTeacher> getListTeachersByListId(String listId);
}
