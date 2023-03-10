package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.DlArrBaseMapper;
import com.company.project.mapper.DlArrSetMapper;
import com.company.project.mapper.DlArrangingCourseMapper;
import com.company.project.mapper.DlCourseTableMapper;
import com.company.project.service.DlArrSetService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dlArrSetService")
public class DlArrSetServiceImpl extends ServiceImpl<DlArrSetMapper, DlArrSetEntity> implements DlArrSetService {
    @Resource
    private DlArrSetMapper mapper;
    @Resource
    private DlArrBaseMapper baseMapper;
    @Resource
    private DlArrangingCourseMapper dlArrangingCourseMapper;
    @Resource
    private DlCourseTableMapper dlCourseTableMapper;

    @Override
    public boolean isOutOfNum(DlArrSetEntity bean) {
        LambdaQueryWrapper<DlArrSetEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlArrSetEntity::getType, bean.getType());
        wrapper.eq(DlArrSetEntity::getTypeId, bean.getTypeId());
        wrapper.eq(DlArrSetEntity::getArrId, bean.getArrId());
        int size = mapper.selectCount(wrapper);
        DlArrangingCourseEntity arrBean = dlArrangingCourseMapper.selectById(bean.getArrId());
        DlArrBaseEntity baseBean = null;
        Integer totalNum = (arrBean.getAmNum() + arrBean.getPmNum() + arrBean.getNightNum()) * arrBean.getWeekDayNum();
        Integer num = 0;
        switch (bean.getType()){
            case 0:
                List<DlArrBaseEntity> baseList = baseMapper.getClassCourseNumList(bean.getArrId());
                if(baseList.size() > 0){
                    if((totalNum - size) <= baseList.get(0).getCourseNum())
                        return true;
                }
                break;
            case 1:
                //查教师课程
                baseBean = new DlArrBaseEntity();
                baseBean.setArrId(bean.getArrId());
                baseBean.setTeacherId(bean.getTypeId());
                num = baseMapper.getTeacherCourseNum(baseBean);

                if((totalNum - size) <= num)
                    return true;
                break;

            case 2:
                //查看班级课程
                baseBean = new DlArrBaseEntity();
                baseBean.setArrId(bean.getArrId());
                baseBean.setClassId(bean.getTypeId());
                num = baseMapper.getClassCourseNum(baseBean);
                if((totalNum - size) <= num)
                    return true;
                break;

            case 3:
                //查看课程不排课
                baseBean = baseMapper.selectById(bean.getTypeId());
                if(totalNum - size <= baseBean.getCourseNum()){
                    return true;
                }
                break;
            case 4:
                baseBean = baseMapper.selectById(bean.getTypeId());
                if(size >= baseBean.getCourseNum()){
                    return true;
                }
                break;
        }


        return false;
    }

    /**
     * Element中的status 1表示不排课， 2表示教师已经排课， 3表示已经在其他排课任务中已经排课了
     * @param typeId
     * @return
     */
    @Override
    public DataResult findPreTableNoArrPosition(String typeId) {
        if(StringUtils.isEmpty(typeId)){
            return DataResult.fail("主键缺失，请重新再试");
        }

        //这里的typeId其实是baseId
        DlArrBaseEntity bean = baseMapper.selectById(typeId);
        Map<String, Element> map = new HashMap<>();
        DlArrSetEntity setBean = new DlArrSetEntity();
        setBean.setArrId(bean.getArrId());
        //查找教师不排课位置
        setBean.setType(1);
        setBean.setTypeId(bean.getTeacherId());
        List<DlArrSetEntity> teacherList = mapper.list(setBean);
        for (DlArrSetEntity b : teacherList){
            put(b.getCol(), b.getRow(), map, 1, "不排课");
        }

        //查找班级不排课位置
        setBean.setType(2);
        setBean.setTypeId(bean.getClassId());
        List<DlArrSetEntity> classList = mapper.list(setBean);
        for (DlArrSetEntity b : classList){
            put(b.getCol(), b.getRow(), map, 1, "不排课");
        }

        //查找整体不排课位置
        setBean.setType(0);
        setBean.setTypeId("0");
        List<DlArrSetEntity> allList = mapper.list(setBean);
        for (DlArrSetEntity b : allList){
            put(b.getCol(), b.getRow(), map, 1, "不排课");
        }
        //查找课程不排课位置
        setBean.setType(3);
        setBean.setTypeId(bean.getId());
        List<DlArrSetEntity> courseList = mapper.list(setBean);
        for (DlArrSetEntity b : courseList){
            put(b.getCol(), b.getRow(), map, 1, "不排课");
        }

        //查找该教师已经预排课的位置--但是不能把该课程的算进来
        LambdaQueryWrapper<DlArrBaseEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlArrBaseEntity::getArrId, bean.getArrId());
        queryWrapper.eq(DlArrBaseEntity::getTeacherId, bean.getTeacherId());
        List<DlArrBaseEntity> baseList = baseMapper.selectList(queryWrapper);

        setBean.setType(4);
        List<DlArrSetEntity> preList = null;
        for (DlArrBaseEntity b: baseList){
            if(b.getId().equals(typeId)){
                continue;
            }
            setBean.setTypeId(b.getId());
            preList = mapper.list(setBean);
            for (DlArrSetEntity s : preList){
                put(s.getCol(), s.getRow(), map, 2, s.getLabel());
            }
        }

        //查找该教师已经在本学期其他排课任务中已经排的课程位置
        DlArrangingCourseEntity arr = dlArrangingCourseMapper.selectById(bean.getArrId());
        LambdaQueryWrapper<DlCourseTableEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlCourseTableEntity::getTermId, arr.getTermId());
        wrapper.ne(DlCourseTableEntity::getArrId, bean.getArrId());
        wrapper.eq(DlCourseTableEntity::getTeacherId, bean.getTeacherId());
        List<DlCourseTableEntity> dlCourseTableEntities = dlCourseTableMapper.selectList(wrapper);
        for(DlCourseTableEntity t: dlCourseTableEntities){
            put(t.getCol(), t.getRow(), map, 3, "已排位");
        }



        List<Element> list = new ArrayList<>();
        for (String key :map.keySet()){
            list.add(map.get(key));
        }
        System.out.println("list.size():" + list.size());
        return DataResult.success(list);
    }


    private void put(Integer col, Integer row, Map<String, Element> map, Integer status, String desc){
        String key = col.toString() + row.toString();
        if(!map.containsKey(key)){
            map.put(key, new Element(col, row, status, desc));
        }
    }
}