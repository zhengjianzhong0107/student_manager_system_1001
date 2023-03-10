package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.DlExamListService;
import com.company.project.vo.resp.DlExamListVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;


@Service("dlExamListService")
public class DlExamListServiceImpl extends ServiceImpl<DlExamListMapper, DlExamListEntity> implements DlExamListService {

    @Resource
    private DlExamListMapper mapper;
    @Resource
    private DlExamTeacherMapper dlExamTeacherMapper;
    @Resource
    private DlExamSubjectMapper dlExamSubjectMapper;
    @Resource
    private DlExamRoomMapper dlExamRoomMapper;
    @Resource
    private DlExamClassMapper dlExamClassMapper;
    @Resource
    private DlExamListTeacherMapper dlExamListTeacherMapper;
    @Resource
    private DlExamPlanMapper dlExamPlanMapper;

    @Override
    public void fastAdd(String examId) {
        DlExamPlanEntity exam = dlExamPlanMapper.selectById(examId);

        //获取所有考场列表
        LambdaQueryWrapper<DlExamRoomEntity> queryRoom = Wrappers.lambdaQuery();
        queryRoom.eq(DlExamRoomEntity::getExamId, examId);
        List<DlExamRoomEntity> room_list = dlExamRoomMapper.selectList(queryRoom);

        //获取所有监考教师列表
        LambdaQueryWrapper<DlExamTeacherEntity> queryTea = Wrappers.lambdaQuery();
        queryTea.eq(DlExamTeacherEntity::getExamId, examId);
        List<DlExamTeacherEntity> tea_list = dlExamTeacherMapper.selectList(queryTea);
        Collections.shuffle(tea_list);//打乱顺序
        //获取考试科目列表
        LambdaQueryWrapper<DlExamSubjectEntity> queryCourse = Wrappers.lambdaQuery();
        queryCourse.eq(DlExamSubjectEntity::getExamId, examId);
        List<DlExamSubjectEntity> course_list = dlExamSubjectMapper.selectList(queryCourse);


        DlExamListEntity listBean = null;
        DlExamListTeacher teacher = null;
        Integer invigilatorNum = exam.getInvigilatorNum();
        int j = 0;
        System.out.println("course_size:" + course_list.size());
        System.out.println("room_size:" + room_list.size());
        System.out.println("invigilatorNum:" + invigilatorNum);
        //开始排考
        for(DlExamSubjectEntity course: course_list){
            for(DlExamRoomEntity room: room_list){
                listBean = new DlExamListEntity();
                listBean.setCourseId(course.getCourseId());
                listBean.setExamPlanId(examId);
                listBean.setEndTime(course.getEndTime());
                listBean.setStartTime(course.getStartTime());
                listBean.setExamTime(course.getExamDate());
                listBean.setExamRoomId(room.getId());
                mapper.insert(listBean);
                for(int i = 0; i < invigilatorNum; i++){
                    DlExamTeacherEntity t = tea_list.get(j);
                    teacher = new DlExamListTeacher();
                    teacher.setExamListId(listBean.getId());
                    teacher.setTeacherId(t.getTeacherId());
                    teacher.setTeacherName(t.getTeacherName());
                    dlExamListTeacherMapper.insert(teacher);
                    j ++;
                    j = j % tea_list.size();
                }
            }
        }


    }
    @Override
    public List<DlExamListEntity> getListByExamId(DlExamListEntity bean){
        System.out.println("exam_list_bean:" + bean);
        return mapper.getListByExamId(bean.getExamPlanId());
    }

    @Override
    public List<DlExamListEntity> getExportListByExamId(String examId){
        List<DlExamListEntity> list = mapper.getListByExamId(examId);
        //获取所有考场列表
        LambdaQueryWrapper<DlExamRoomEntity> queryRoom = Wrappers.lambdaQuery();
        queryRoom.eq(DlExamRoomEntity::getExamId, examId);
        List<DlExamRoomEntity> room_list = dlExamRoomMapper.selectList(queryRoom);

        //获取考试科目列表
        LambdaQueryWrapper<DlExamSubjectEntity> queryCourse = Wrappers.lambdaQuery();
        queryCourse.eq(DlExamSubjectEntity::getExamId, examId);
        List<DlExamSubjectEntity> course_list = dlExamSubjectMapper.selectList(queryCourse);
        for(DlExamListEntity li: list){
            for(DlExamRoomEntity room: room_list){
                if(li.getExamRoomId().equals(room.getId()))
                    li.setExamRoomId(room.getRoomName()+ "(" + room.getClassRoomName() + ")");
            }

            for(DlExamSubjectEntity cou: course_list){
                if(cou.getCourseId().equals(li.getCourseId()))
                    li.setCourseId(cou.getCourseName());
            }
        }
        return list;
    }

    @Override
    public void removeListByExamId(String examId){
        mapper.removeTeachersByExamId(examId);
        mapper.removeListByExamId(examId);
    }

    @Override
    public DataResult getMyExamPlanList(DlExamListEntity bean) {
        if(StringUtils.isEmpty(bean.getTeacherId())){
            return DataResult.fail("登陆失效，请重新登陆");
        }
        List<DlExamListVo> list = mapper.getMyExamPlanList(bean);
        return DataResult.success(list);
    }

    @Override
    public void removeListTeacherByTeacherId(String listTeacherId) {
        mapper.removeTeacherByTeacherId(listTeacherId);
    }

    /**
     * 通过列表ID查询旗下的所有监考教师列表
     * @param listId
     * @return
     */
    @Override
    public List<DlExamListTeacher> getListTeachersByListId(String listId){
        return mapper.getListTeachersByListId(listId);
    }

    @Override
    public void saveTeacher(DlExamListTeacher bean){
        dlExamListTeacherMapper.insert(bean);
    }

    @Override
    public void updateTeacher(DlExamListTeacher bean) {
        dlExamListTeacherMapper.updateById(bean);
    }
}