package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.GradeUtils;
import com.company.project.common.utils.MapUtils;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dlArrBaseService")
public class DlArrBaseServiceImpl extends ServiceImpl<DlArrBaseMapper, DlArrBaseEntity> implements DlArrBaseService {
    @Resource
    private DlArrBaseMapper mapper;
    @Resource
    private DlArrangingCourseMapper dlArrangingCourseMapper;
    @Resource
    private DlClassTeacherMapper dlClassTeacherMapper;
    @Resource
    private DlClassService classService;
    @Resource
    private UserService userService;
    @Resource
    private DlCourseService courseService;
    @Resource
    private DlRoomService dlRoomService;
    @Resource
    private DlArrSetMapper dlArrSetMapper;





    @Override
    public DataResult copyClassTeacherToThisBean(String arrId) {
        //
        if(StringUtils.isEmpty(arrId)){
            return DataResult.fail("arrId缺失");
        }
        deleteByArrId(arrId);
        //删除预排课和不排课信息
        LambdaQueryWrapper<DlArrSetEntity> setWrapper = Wrappers.lambdaQuery();
        setWrapper.eq(DlArrSetEntity::getArrId, arrId);
        dlArrSetMapper.delete(setWrapper);


        //查出该排课任务中的参排年级信息



        DlArrangingCourseEntity arr = dlArrangingCourseMapper.selectById(arrId);


        Map<String, String> map = new HashMap<>();
        map.put("arrId", arrId);
        map.put("termId", arr.getTermId());
        List<DlClassTeacherEntity> ctList = dlClassTeacherMapper.getArrCourseClassTeacherListByArrIdAndTermId(map);
        //检查是否已经
        if(ctList.size() == 0){
            return DataResult.fail("还未创建班级课程信息,请到--班级课程--中添加本学期的班级课程教学信息");
        }
        Map<String, SysUser> teacherMap = getTeacherMap();
        Map<String, DlClassEntity> classMap = getClassMap();
        Map<String, DlCourseEntity> courseMap = getCourseMap();
        Map<String, DlRoomEntity> roomMap = MapUtils.getMapByList(dlRoomService.list());

        System.out.println("teacherMap:" + teacherMap);

        DlArrBaseEntity entity = null;
        SysUser user = null;
        DlClassEntity clazz = null;
        DlCourseEntity course = null;
        DlRoomEntity room = null;


        for (DlClassTeacherEntity bean : ctList){
            entity = new DlArrBaseEntity();
            entity.setArrId(arrId);
            entity.setClassId(bean.getClassId());
            entity.setCourseId(bean.getCourseId());
            entity.setTeacherId(bean.getTeacherId());

            user = teacherMap.get(bean.getTeacherId());
            if(user != null)
                entity.setTeacherName(user.getRealName());
            clazz = classMap.get(bean.getClassId());
            if(clazz != null){
                entity.setClassName(GradeUtils.getNameByGradeNum(clazz.getGradeNum()) + clazz.getClassNo() + "班");
                room = roomMap.get(clazz.getRoomId());
                if(room != null){
                    entity.setRoomId(room.getId());
                    entity.setRoomName(room.getRoomName());
                }
            }
            course = courseMap.get(bean.getCourseId());
            if(course != null){
                entity.setCourseName(course.getName());
                entity.setCourseNum(course.getWeekNum());
                entity.setCourseType(course.getCourseType());
            }

            mapper.insert(entity);

        }
        return DataResult.success();
    }

    @Override
    public DataResult getTeacherList(DlArrBaseEntity bean) {
        if(StringUtils.isEmpty(bean.getArrId())){
            return DataResult.fail("arrId主键缺失,请重新再试");
        }
        LambdaQueryWrapper<DlArrBaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlArrBaseEntity::getArrId, bean.getArrId());
        if(!StringUtils.isEmpty(bean.getTeacherName())){
            wrapper.like(DlArrBaseEntity::getTeacherName, bean.getTeacherName());
        }
        wrapper.groupBy(DlArrBaseEntity::getTeacherId);

        List<DlArrBaseEntity> list = mapper.selectList(wrapper);

        return DataResult.success(list);
    }

    @Override
    public DataResult getCourseList(DlArrBaseEntity bean) {
        if(StringUtils.isEmpty(bean.getArrId())){
            return DataResult.fail("arrId主键缺失,请重新再试");
        }
        LambdaQueryWrapper<DlArrBaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlArrBaseEntity::getArrId, bean.getArrId());
        wrapper.and(i -> i.like(DlArrBaseEntity::getCourseName, bean.getCourseName())
                .or().like(DlArrBaseEntity::getClassName, bean.getCourseName())
                .or().like(DlArrBaseEntity::getTeacherName, bean.getCourseName()));
        return DataResult.success(mapper.selectList(wrapper));
    }

    @Override
    public DataResult getClassList(DlArrBaseEntity bean) {
        if(StringUtils.isEmpty(bean.getArrId())){
            return DataResult.fail("arrId主键缺失,请重新再试");
        }
        LambdaQueryWrapper<DlArrBaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlArrBaseEntity::getArrId, bean.getArrId());
        wrapper.like(DlArrBaseEntity::getClassName, bean.getClassName());
        wrapper.groupBy(DlArrBaseEntity::getClassId);
        return DataResult.success(mapper.selectList(wrapper));
    }


    public Map<String, SysUser> getTeacherMap(){
        List<SysUser> list = userService.getTeacherList();
        return MapUtils.getMapByList(list);
    }

    public Map<String, DlClassEntity> getClassMap(){
        List<DlClassEntity> list = classService.getList();
        return MapUtils.getMapByList(list);
    }

    public Map<String, DlCourseEntity> getCourseMap(){
        List<DlCourseEntity> list = courseService.list();
        return MapUtils.getMapByList(list);
    }


    public void deleteByArrId(String arrId){
        LambdaQueryWrapper<DlArrBaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlArrBaseEntity::getArrId, arrId);
        mapper.delete(wrapper);
    }













}