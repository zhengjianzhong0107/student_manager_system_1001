package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DlCourseEntity;
import com.company.project.mapper.DlCourseMapper;
import com.company.project.service.DlCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("dlCourseService")
public class DlCourseServiceImpl extends ServiceImpl<DlCourseMapper, DlCourseEntity> implements DlCourseService {
    @Resource
    private DlCourseMapper mapper;

    public JSONArray getJSONList(){
        LambdaQueryWrapper<DlCourseEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(true, DlCourseEntity::getGrade);
        return JSONArray.parseArray(JSON.toJSONString(mapper.selectList(queryWrapper)));
    }

    @Override
    public List<DlCourseEntity> getListByGrade(String grade){
        LambdaQueryWrapper<DlCourseEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlCourseEntity::getGrade, grade);

        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<DlCourseEntity> getListByClassType(Integer classType){
        LambdaQueryWrapper<DlCourseEntity> queryWrapper = Wrappers.lambdaQuery();
        if(classType != null){
            queryWrapper.eq(DlCourseEntity::getGrade, classType);
        }else{
            return null;
        }
        return mapper.selectList(queryWrapper);
    }



}