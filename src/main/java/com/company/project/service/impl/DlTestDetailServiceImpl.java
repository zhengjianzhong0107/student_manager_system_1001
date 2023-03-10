package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlTestDetailMapper;
import com.company.project.entity.DlTestDetailEntity;
import com.company.project.service.DlTestDetailService;


@Service("dlTestDetailService")
public class DlTestDetailServiceImpl extends ServiceImpl<DlTestDetailMapper, DlTestDetailEntity> implements DlTestDetailService {


}