package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlArrBaseEntity;

import java.util.List;

/**
 * 
 * 
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-21 14:29:57
 */
public interface DlArrBaseMapper extends BaseMapper<DlArrBaseEntity> {
    /**
     * 查询超课教师列表
     * @param arrId
     * @return
     */
    List<DlArrBaseEntity> getTeacherCourseNumList(String arrId);

    /**
     * 获取教师的课程数
     * @param bean
     * @return
     */
    Integer getTeacherCourseNum(DlArrBaseEntity bean);

    /**
     * 查询超课班级列表
     * @param arrId
     * @return
     */
    List<DlArrBaseEntity> getClassCourseNumList(String arrId);

    /**
     * 获取班级的课程数
     * @param bean
     * @return
     */
    Integer getClassCourseNum(DlArrBaseEntity bean);
}
