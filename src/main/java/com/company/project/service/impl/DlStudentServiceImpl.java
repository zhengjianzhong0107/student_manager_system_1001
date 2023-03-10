package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlStudentMapper;
import com.company.project.entity.DlStudentEntity;
import com.company.project.service.DlStudentService;

import javax.annotation.Resource;


@Service("dlStudentService")
public class DlStudentServiceImpl extends ServiceImpl<DlStudentMapper, DlStudentEntity> implements DlStudentService {
    @Resource
    private DlStudentMapper mapper;

    @Override
    public Integer getClassTypeByStudentId(String studentId) {
        return mapper.getClassTypeByStudentId(studentId);
    }
}