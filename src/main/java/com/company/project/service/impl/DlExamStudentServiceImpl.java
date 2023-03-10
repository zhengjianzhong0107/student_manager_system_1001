package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlExamStudentMapper;
import com.company.project.entity.DlExamStudentEntity;
import com.company.project.service.DlExamStudentService;


@Service("dlExamStudentService")
public class DlExamStudentServiceImpl extends ServiceImpl<DlExamStudentMapper, DlExamStudentEntity> implements DlExamStudentService {


}