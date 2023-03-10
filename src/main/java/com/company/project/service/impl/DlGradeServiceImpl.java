package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.Constant;
import com.company.project.common.utils.NumberUtils;
import com.company.project.entity.DlClassEntity;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.service.RedisService;
import com.company.project.service.SysDictService;
import com.company.project.vo.resp.MenuTreeVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlGradeMapper;
import com.company.project.entity.DlGradeEntity;
import com.company.project.service.DlGradeService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service("dlGradeService")
public class DlGradeServiceImpl extends ServiceImpl<DlGradeMapper, DlGradeEntity> implements DlGradeService {
    @Resource
    private DlGradeMapper mapper;

    @Resource
    private RedisService redisService;

    @Resource
    private SysDictService sysDictService;

    public JSONArray getGrades(){
        if(!redisService.exists(Constant.GRADE_DATA_KEY)){
            this.updateRedisGradeData();
        }
        String data = redisService.get(Constant.GRADE_DATA_KEY);
        return JSONArray.parseArray(data);
    }

    @Override
    public void updateRedisGradeData(){
        LambdaQueryWrapper<DlGradeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlGradeEntity::getStatus, 1);
        queryWrapper.orderByDesc(true, DlGradeEntity::getType);
        queryWrapper.orderByDesc(true, DlGradeEntity::getGradeName);
        List<DlGradeEntity> list = mapper.selectList(queryWrapper);
        redisService.set(Constant.GRADE_DATA_KEY, JSON.toJSONString(list));
    }

    /**
     * 获取年级及其下的班级列表信息
     * @return
     */
    @Override
    public List<DlGradeEntity> getGradeList(){
        String grades = redisService.get(Constant.GRADE_DATA_KEY);
        String classes = redisService.get(Constant.CLASS_DATA_KEY);
        List<DlClassEntity> classList = JSONObject.parseArray(classes, DlClassEntity.class);
        List<DlGradeEntity> gradeList = JSONObject.parseArray(grades, DlGradeEntity.class);
        List<DlClassEntity> list = null;
        for(DlGradeEntity grade: gradeList){
            list = new ArrayList<>();
            for(DlClassEntity clazz: classList){
                if(clazz.getGradeId().equals(grade.getId())){
                    list.add(clazz);
                    //classList.remove(clazz);
                }
            }
            grade.setClassList(list);
        }
        return gradeList;
    }


    @Override
    public List<MenuTreeVo> getClassTree(String classType){

        List<SysDictDetailEntity> list = sysDictService.getListByType("class_type", false);
        if(!StringUtils.isEmpty(classType)){
            for(SysDictDetailEntity bean: list){
                if(!bean.getValue().equals(classType)){
                    list.remove(bean);
                }
            }
        }
        List<DlGradeEntity> gradeList = this.getGradeList();
        List<MenuTreeVo> child1List = new ArrayList<>();
        for(SysDictDetailEntity dic: list){
            MenuTreeVo classTreeVo1 = new MenuTreeVo();
            List<MenuTreeVo> child2List = new ArrayList<>();
            classTreeVo1.setTitle(dic.getLabel());
            for(DlGradeEntity g: gradeList){
                if(g.getType().toString().equals(dic.getValue())){
                    MenuTreeVo classTreeVo2 = new MenuTreeVo();
                    classTreeVo2.setTitle(g.getGradeName() + "级");
                    classTreeVo2.setSpread(false);
                    List<MenuTreeVo> child3List = new ArrayList<>();
                    for(DlClassEntity c :g.getClassList()){
                        MenuTreeVo classTreeVo3 = new MenuTreeVo();
                        classTreeVo3.setTitle(c.getClassNo() + "班");
                        classTreeVo3.setId(c.getId());
                        child3List.add(classTreeVo3);
                    }
                    classTreeVo2.setChildren(child3List);
                    child2List.add(classTreeVo2);
                }
            }
            classTreeVo1.setChildren(child2List);
            child1List.add(classTreeVo1);
        }
        return child1List;
    }

    @Override
    public List<MenuTreeVo> getGradeTree(){
        List<SysDictDetailEntity> list = sysDictService.getListByType("grade", true);
        List<SysDictDetailEntity> typeList = sysDictService.getListByType("class_type", true);
        List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
        List<MenuTreeVo> childList = null;
        MenuTreeVo v = null;
        boolean bool = true;//用于标记判断是否后面的需要展开
        for (SysDictDetailEntity type: typeList){
            childList = new ArrayList<>();
            for(SysDictDetailEntity d: list){
                //如果年级列表中的grade的value取千位值和class_type中的值相等，那么表示该年级是属于该所属阶段的
                if(NumberUtils.isInteger(d.getValue()) && type.getValue().equals((Integer.parseInt(d.getValue())/1000) + "")){
                    v = new MenuTreeVo();
                    v.setId(d.getValue());
                    v.setTitle(d.getLabel());
                    v.setBean(d);
                    childList.add(v);
                }
            }
            v = new MenuTreeVo();
            v.setId(type.getValue());
            if(bool){
                bool = false;
            }else{
                v.setSpread(false);
            }
            v.setTitle(type.getLabel());
            v.setBean(type);
            v.setChildren(childList);
            menuTreeVoList.add(v);
        }
        return menuTreeVoList;
    }

    @Override
    public List<MenuTreeVo> getGradeTree2(){
        String grades = redisService.get(Constant.GRADE_DATA_KEY);
        List<DlGradeEntity> gradeList = JSONObject.parseArray(grades, DlGradeEntity.class);
        List<SysDictDetailEntity> typeList = sysDictService.getListByType("class_type", true);
        List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
        List<MenuTreeVo> childList = null;
        MenuTreeVo v = null;
        for(SysDictDetailEntity type: typeList){
            childList = new ArrayList<>();
            for (DlGradeEntity grade: gradeList){
                if(grade.getType().toString().equals(type.getValue())){
                    v = new MenuTreeVo();
                    v.setId(grade.getId());
                    v.setTitle(grade.getGradeName() + "级");
                    v.setBean(grade);
                    childList.add(v);
                }
            }
            v = new MenuTreeVo();
            v.setId(type.getValue());
            v.setTitle(type.getLabel());
            v.setChildren(childList);
            menuTreeVoList.add(v);
        }
        return menuTreeVoList;
    }






}