package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlExamSubjectEntity;

import java.util.List;

/**
 * 
 * 
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
public interface DlExamSubjectMapper extends BaseMapper<DlExamSubjectEntity> {
    /**
     * 检查是否未设置完成的条数
     * @param examId
     * @return
     */
	int isNotCompleteCount(String examId);

    /**
     * 查询所有存在时间冲突的数据
     * @param bean
     * @return
     */
	List<DlExamSubjectEntity> timeIsOverlap(DlExamSubjectEntity bean);
}
