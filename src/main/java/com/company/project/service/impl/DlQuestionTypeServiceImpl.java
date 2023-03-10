package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DlQuestionTypeEntity;
import com.company.project.mapper.DlQuestionTypeMapper;
import com.company.project.service.DlQuestionTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("dlQuestionTypeService")
public class DlQuestionTypeServiceImpl extends ServiceImpl<DlQuestionTypeMapper, DlQuestionTypeEntity> implements DlQuestionTypeService {
    @Resource
    private DlQuestionTypeMapper mapper;

    public JSONArray getList(){
        LambdaQueryWrapper<DlQuestionTypeEntity> queryWrapper = Wrappers.lambdaQuery();
        return JSONArray.parseArray(JSON.toJSONString(mapper.selectList(queryWrapper)));
    }

}