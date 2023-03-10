package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlExamRoomEntity;

import java.util.Map;

/**
 * 
 * 
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
public interface DlExamRoomMapper extends BaseMapper<DlExamRoomEntity> {
    /**
     * 获取考试计划中的所有考场的容量
     * @param examId
     * @return
     */
    Integer getRoomsCapacity(String examId);

    /**
     * 通过学生ID和考试计划ID查询该学生在那个考室里
     * @param map 学生ID和考试计划ID
     * @return
     */
    DlExamRoomEntity getExamRoomBeanByStuId(Map<String, Object> map);
}
