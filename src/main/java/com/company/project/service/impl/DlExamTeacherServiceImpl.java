package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.DlExamTeacherService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("dlExamTeacherService")
public class DlExamTeacherServiceImpl extends ServiceImpl<DlExamTeacherMapper, DlExamTeacherEntity> implements DlExamTeacherService {
    @Resource
    private DlExamTeacherMapper mapper;
    @Resource
    private DlExamSubjectMapper dlExamSubjectMapper;
    @Resource
    private DlExamPlanMapper dlExamPlanMapper;
    @Resource
    private DlClassTeacherMapper dlClassTeacherMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void fastAddTeachers(String examId) {
        LambdaQueryWrapper<DlExamSubjectEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamSubjectEntity::getExamId, examId);
        DlExamPlanEntity plan = dlExamPlanMapper.selectById(examId);
        List<DlClassTeacherEntity> classTeacherList = dlClassTeacherMapper.getListByGradeId(plan.getGradeId());
        List<DlExamSubjectEntity> list = dlExamSubjectMapper.selectList(queryWrapper);
        DlExamTeacherEntity bean = null;
        SysUser user = null;
        DlExamTeacherEntity entity = new DlExamTeacherEntity();

        entity.setExamId(plan.getId());
        for(DlExamSubjectEntity subject: list){
            for(DlClassTeacherEntity tea: classTeacherList){
                if(subject.getCourseId().equals(tea.getCourseId())){
                    entity.setTeacherId(tea.getTeacherId());
                    int count = mapper.getCountByExamIdAndTeacherId(entity);//检查是否已经添加了该老师，避免重复添加
                    if(count == 0){
                        bean = new DlExamTeacherEntity();
                        bean.setExamId(plan.getId());
                        bean.setTeacherId(tea.getTeacherId());
                        user = sysUserMapper.selectById(tea.getTeacherId());
                        bean.setTeacherName(user.getRealName());
                        bean.setUsername(user.getUsername());
                        mapper.insert(bean);
                    }
                }
            }
        }
    }

    @Override
    public List<DlExamTeacherEntity> getListByExamId(String examId){
        LambdaQueryWrapper<DlExamTeacherEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamTeacherEntity::getExamId, examId);

        return mapper.selectList(queryWrapper);
    }
}