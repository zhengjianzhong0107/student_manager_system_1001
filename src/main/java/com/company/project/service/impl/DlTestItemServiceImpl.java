package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlTestItemMapper;
import com.company.project.entity.DlTestItemEntity;
import com.company.project.service.DlTestItemService;


@Service("dlTestItemService")
public class DlTestItemServiceImpl extends ServiceImpl<DlTestItemMapper, DlTestItemEntity> implements DlTestItemService {


}