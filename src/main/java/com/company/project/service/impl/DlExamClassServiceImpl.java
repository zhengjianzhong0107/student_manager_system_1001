package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlExamClassMapper;
import com.company.project.entity.DlExamClassEntity;
import com.company.project.service.DlExamClassService;


@Service("dlExamClassService")
public class DlExamClassServiceImpl extends ServiceImpl<DlExamClassMapper, DlExamClassEntity> implements DlExamClassService {


}