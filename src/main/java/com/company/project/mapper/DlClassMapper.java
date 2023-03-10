package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlClassEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlClassMapper extends BaseMapper<DlClassEntity> {
    /**
     * 获取各个年级的在校人数
     * @return
     */
    List<DlClassEntity> getGradeStudentNumAndGradeName();

    DlClassEntity getBeanByMap(Map<String, Object> map);
}
