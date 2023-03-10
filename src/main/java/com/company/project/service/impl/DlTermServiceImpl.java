package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DlTermEntity;
import com.company.project.mapper.DlTermMapper;
import com.company.project.service.DlTermService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service("dlTermService")
public class DlTermServiceImpl extends ServiceImpl<DlTermMapper, DlTermEntity> implements DlTermService {
    public JSONArray getJSONList(){
        DlTermEntity bean = new DlTermEntity();
        bean.setStatus(1);
        List<DlTermEntity> termList = this.getListByConditions(bean);
        return JSONArray.parseArray(JSON.toJSONString(termList, SerializerFeature.DisableCircularReferenceDetect));
    }

    public JSONArray getAllJSONList(){
        DlTermEntity bean = new DlTermEntity();
        List<DlTermEntity> termList = this.getListByConditions(bean);
        return JSONArray.parseArray(JSON.toJSONString(termList, SerializerFeature.DisableCircularReferenceDetect));
    }


    public List<DlTermEntity> getListByConditions(DlTermEntity bean){
        LambdaQueryWrapper<DlTermEntity> queryWrapper = Wrappers.lambdaQuery();
        if(bean.getStatus() != null){
            queryWrapper.eq(DlTermEntity::getStatus, bean.getStatus());
        }
        if(!StringUtils.isEmpty(bean.getTitle())){
            queryWrapper.like(DlTermEntity::getTitle, bean.getTitle());
        }
        return this.list(queryWrapper);
    }

}