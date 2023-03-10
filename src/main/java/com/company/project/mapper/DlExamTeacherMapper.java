package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlExamTeacherEntity;

/**
 * 
 * 
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-28 13:09:20
 */
public interface DlExamTeacherMapper extends BaseMapper<DlExamTeacherEntity> {
	int getCountByExamIdAndTeacherId(DlExamTeacherEntity bean);
}
