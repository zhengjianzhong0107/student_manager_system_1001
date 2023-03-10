package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlCourseTeacherMapper;
import com.company.project.entity.DlCourseTeacherEntity;
import com.company.project.service.DlCourseTeacherService;


@Service("dlCourseTeacherService")
public class DlCourseTeacherServiceImpl extends ServiceImpl<DlCourseTeacherMapper, DlCourseTeacherEntity> implements DlCourseTeacherService {


}