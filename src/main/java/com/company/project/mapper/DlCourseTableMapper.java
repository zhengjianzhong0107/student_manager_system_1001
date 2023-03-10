package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlCourseTableEntity;

import java.util.List;

/**
 * 
 * 
 * @author hui
 * @email *****@mail.com
 * @date 2021-02-07 10:45:06
 */
public interface DlCourseTableMapper extends BaseMapper<DlCourseTableEntity> {
    void deleteByArrId(String arrId);

    /**
     * 获取所有该学期的教师排课数量，排课数量存在week字段中
     * @param termId
     * @return
     */
    List<DlCourseTableEntity> getTermCountGroupByTeacherId(String termId);

    /**
     * 获取数据条数
     * @param bean
     * @return
     */
    Integer getCountByConditions(DlCourseTableEntity bean);
}
