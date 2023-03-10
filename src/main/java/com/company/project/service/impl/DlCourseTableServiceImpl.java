package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrangingCourseEntity;
import com.company.project.entity.DlCourseTableEntity;
import com.company.project.entity.Element;
import com.company.project.mapper.DlArrangingCourseMapper;
import com.company.project.mapper.DlCourseTableMapper;
import com.company.project.service.DlCourseTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service("dlCourseTableService")
public class DlCourseTableServiceImpl extends ServiceImpl<DlCourseTableMapper, DlCourseTableEntity> implements DlCourseTableService {
    @Resource
    private DlCourseTableMapper mapper;
    @Resource
    private DlArrangingCourseMapper dlArrangingCourseMapper;

    @Override
    public DataResult findAllowPosition(String id) {
        DlCourseTableEntity bean = mapper.selectById(id);
        DlArrangingCourseEntity arrBean = dlArrangingCourseMapper.selectById(bean.getArrId());

        LambdaQueryWrapper<DlCourseTableEntity> wrapper = Wrappers.lambdaQuery();

        wrapper.eq(DlCourseTableEntity::getTermId, bean.getTermId());//查询整个学期中的课程
        wrapper.eq(DlCourseTableEntity::getTeacherId, bean.getTeacherId());
        List<DlCourseTableEntity> teacherList = mapper.selectList(wrapper);
        List<Element> elements = new ArrayList<>();
        //找出该教师没有课的时间
        int length = arrBean.getAmNum() + arrBean.getPmNum() + (arrBean.getNightNum() == null ? 0 : arrBean.getNightNum());
        for(int i = 1; i <= arrBean.getWeekDayNum(); i ++){
            for(int j = 1; j <= length; j ++){
                boolean bool = false;
                for(DlCourseTableEntity e: teacherList){
                    if(e.getCol() == i && e.getRow() == j){
                        bool = true;
                        break;
                    }
                }
                if(!bool){
                    elements.add(new Element(i, j));
                }
            }
        }
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()){
            Element e = iterator.next();
            wrapper = Wrappers.lambdaQuery();
            wrapper.eq(DlCourseTableEntity::getTermId, bean.getTermId());
            wrapper.eq(DlCourseTableEntity::getClassId, bean.getClassId());
            wrapper.eq(DlCourseTableEntity::getCol, e.getCol());
            wrapper.eq(DlCourseTableEntity::getRow, e.getRow());
            List<DlCourseTableEntity> classList = mapper.selectList(wrapper);//找出班级中的课表中在本位置是否有课，如果有课，就查看该教师在被调课教师调课位置是否有课
            if(classList != null && classList.size() != 0){
                wrapper = Wrappers.lambdaQuery();
                wrapper.eq(DlCourseTableEntity::getTermId, bean.getTermId());
                wrapper.eq(DlCourseTableEntity::getTeacherId, classList.get(0).getTeacherId());
                wrapper.eq(DlCourseTableEntity::getCol, bean.getCol());
                wrapper.eq(DlCourseTableEntity::getRow, bean.getRow());
                List<DlCourseTableEntity> tempList = mapper.selectList(wrapper);//查找要被调整的教师该位置是否有课程，如果没有，那么就表示可以调课
                if(tempList != null && tempList.size() != 0){
                    iterator.remove();
                }
            }
        }


        return DataResult.success(elements);
    }




}