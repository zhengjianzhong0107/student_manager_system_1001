package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlClassTeacherEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlClassTeacherMapper extends BaseMapper<DlClassTeacherEntity> {
    List<DlClassTeacherEntity> getListByGradeId(String gradeId);

    /**
     * 通过排课任务ID查询班级教师列表信息--（排课任务中选中的年级的班级教师列表信息）
     * @param map -- map中必须包含arrId 和 termId两个
     * @return
     */
    List<DlClassTeacherEntity> getArrCourseClassTeacherListByArrIdAndTermId(Map<String, String> map);
}
