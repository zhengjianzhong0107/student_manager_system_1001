package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.Constant;
import com.company.project.common.utils.NumberUtils;
import com.company.project.entity.DlClassEntity;
import com.company.project.entity.DlGradeEntity;
import com.company.project.mapper.DlClassMapper;
import com.company.project.service.DlClassService;
import com.company.project.service.DlGradeService;
import com.company.project.service.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dlClassService")
public class DlClassServiceImpl extends ServiceImpl<DlClassMapper, DlClassEntity> implements DlClassService {
    @Resource
    private DlClassMapper mapper;

    @Resource
    private RedisService redisService;

    @Resource
    private DlGradeService dlGradeService;


    public JSONArray getClassList(){
        List<DlClassEntity> classList = this.getClassDetailsList();
        return JSONArray.parseArray(JSON.toJSONString(classList, SerializerFeature.DisableCircularReferenceDetect));
    }


    @Override
    public List<DlClassEntity> getClassDetailsList(){
        if(!redisService.exists(Constant.CLASS_DATA_KEY)){
            this.updateRedisClassData();
        }
        if(!redisService.exists(Constant.GRADE_DATA_KEY)){
            dlGradeService.updateRedisGradeData();
        }
        String classData = redisService.get(Constant.CLASS_DATA_KEY);
        String gradeData = redisService.get(Constant.GRADE_DATA_KEY);
        List<DlClassEntity> classList = JSONObject.parseArray(classData, DlClassEntity.class);
        List<DlGradeEntity> gradeList = JSONObject.parseArray(gradeData, DlGradeEntity.class);
        for(DlClassEntity classEntity: classList){
            for(DlGradeEntity grade: gradeList){
                if(classEntity.getGradeId().equals(grade.getId())){
                    classEntity.setGrade(grade);
                }
            }
        }
        return classList;
    }




    public List<DlClassEntity> getList(){
        if(!redisService.exists(Constant.CLASS_DATA_KEY)){
            this.updateRedisClassData();
        }
        String classData = redisService.get(Constant.CLASS_DATA_KEY);
        return JSONObject.parseArray(classData, DlClassEntity.class);
    }

    @Override
    public DlClassEntity getBeanByStr(String className) {
        int type = 0;
        if(className.contains("小")){
            type = 1;
        }else if(className.contains("初")){
            type = 2;
        }else if(className.contains("高")){
            type = 3;
        }
        className = NumberUtils.replaceChineseNumberToInt(className);
        //获取年级
        int jiIndex = className.indexOf("级");
        if(jiIndex == -1){
            return null;
        }

        int banIndex = className.indexOf("班");
        if(banIndex == -1){
            return null;
        }
        List<Integer> jiList = NumberUtils.getNumbersFromStr(className.substring(0, jiIndex));
        Integer ji = null;
        if(jiList.size() > 0){
            ji = jiList.get(0);
        }else{
            return null;
        }

        List<Integer> banList = NumberUtils.getNumbersFromStr(className.substring(jiIndex + 1, banIndex));
        Integer ban = null;
        if(banList.size() > 0){
            ban = banList.get(0);
        }else {
            return null;
        }

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("grade", ji);
        conditions.put("classNo", NumberUtils.toChinese(ban + ""));
        conditions.put("type", type);

        return mapper.getBeanByMap(conditions);
    }

    @Override
    public List<DlClassEntity> getGradeStudentNumAndGradeName() {
        return mapper.getGradeStudentNumAndGradeName();
    }


    public void updateRedisClassData(){
        LambdaQueryWrapper<DlClassEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlClassEntity::getStatus, 1);
        queryWrapper.orderByAsc(true, DlClassEntity::getClassType);
        queryWrapper.orderByAsc(true, DlClassEntity::getClassNo);
        List<DlClassEntity> list = mapper.selectList(queryWrapper);
        redisService.set(Constant.CLASS_DATA_KEY, JSON.toJSONString(list));
    }


}