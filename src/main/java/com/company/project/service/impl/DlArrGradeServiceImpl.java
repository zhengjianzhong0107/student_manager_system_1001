package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlArrGradeMapper;
import com.company.project.entity.DlArrGradeEntity;
import com.company.project.service.DlArrGradeService;


@Service("dlArrGradeService")
public class DlArrGradeServiceImpl extends ServiceImpl<DlArrGradeMapper, DlArrGradeEntity> implements DlArrGradeService {


}