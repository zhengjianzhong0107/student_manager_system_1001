package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlClassTeacherMapper;
import com.company.project.entity.DlClassTeacherEntity;
import com.company.project.service.DlClassTeacherService;


@Service("dlClassTeacherService")
public class DlClassTeacherServiceImpl extends ServiceImpl<DlClassTeacherMapper, DlClassTeacherEntity> implements DlClassTeacherService {


}