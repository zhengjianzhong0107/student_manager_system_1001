package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.service.SysDictService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlSubjectMapper;
import com.company.project.entity.DlSubjectEntity;
import com.company.project.service.DlSubjectService;

import javax.annotation.Resource;
import java.util.List;


@Service("dlSubjectService")
public class DlSubjectServiceImpl extends ServiceImpl<DlSubjectMapper, DlSubjectEntity> implements DlSubjectService {
    @Resource
    private DlSubjectMapper mapper;
    @Resource
    private SysDictService sysDictService;

    /**
     * 获取学科列表信息
     * @return
     */
    public JSONArray getSubjects(){
        LambdaQueryWrapper<DlSubjectEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(true, DlSubjectEntity::getClassType);
        List<DlSubjectEntity> list = mapper.selectList(queryWrapper);
        List<SysDictDetailEntity> dictDetailEntities = sysDictService.getListByType("class_type", false);
        for(DlSubjectEntity s: list){
            for(SysDictDetailEntity e: dictDetailEntities){
                if(s.getClassType().toString().equals(e.getValue())){
                    s.setClassTypeBean(e);
                }
            }
        }
        return JSONArray.parseArray(JSON.toJSONString(list));
    }

    @Override
    public List<DlSubjectEntity> getListByClassType(Integer classType){
        LambdaQueryWrapper<DlSubjectEntity> queryWrapper = Wrappers.lambdaQuery();
        if(classType != null){
            classType = classType / 1000;//取千位值，（千位值表示所属阶段）
            queryWrapper.eq(DlSubjectEntity::getClassType, classType);
        }else{
            return null;
        }
        return mapper.selectList(queryWrapper);
    }

}