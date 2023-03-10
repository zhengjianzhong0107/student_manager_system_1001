package com.company.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DlExamSubjectEntity;
import com.company.project.mapper.DlExamSubjectMapper;
import com.company.project.service.DlExamSubjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("dlExamSubjectService")
public class DlExamSubjectServiceImpl extends ServiceImpl<DlExamSubjectMapper, DlExamSubjectEntity> implements DlExamSubjectService {
    @Resource
    private DlExamSubjectMapper mapper;

    @Override
    public boolean checkSetIsComplete(String examId) {
        int count = mapper.isNotCompleteCount(examId);
        if(count == 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean timeIsOverlap(DlExamSubjectEntity bean) {
        List<DlExamSubjectEntity> list = mapper.timeIsOverlap(bean);
        if(list.size() > 0){
            return true;
        }
        return false;
    }
}