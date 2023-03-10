package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlExamResultMapper;
import com.company.project.entity.DlExamResultEntity;
import com.company.project.service.DlExamResultService;


@Service("dlExamResultService")
public class DlExamResultServiceImpl extends ServiceImpl<DlExamResultMapper, DlExamResultEntity> implements DlExamResultService {


}