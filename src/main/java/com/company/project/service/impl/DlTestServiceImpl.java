package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlTestMapper;
import com.company.project.entity.DlTestEntity;
import com.company.project.service.DlTestService;


@Service("dlTestService")
public class DlTestServiceImpl extends ServiceImpl<DlTestMapper, DlTestEntity> implements DlTestService {


}