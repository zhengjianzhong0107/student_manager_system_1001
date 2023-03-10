package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlQuestionBankMapper;
import com.company.project.entity.DlQuestionBankEntity;
import com.company.project.service.DlQuestionBankService;


@Service("dlQuestionBankService")
public class DlQuestionBankServiceImpl extends ServiceImpl<DlQuestionBankMapper, DlQuestionBankEntity> implements DlQuestionBankService {


}