package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.DlExamPlanService;
import com.company.project.service.DlExamRoomService;
import com.company.project.vo.resp.DLExamPlanVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service("dlExamPlanService")
public class DlExamPlanServiceImpl extends ServiceImpl<DlExamPlanMapper, DlExamPlanEntity> implements DlExamPlanService {
    @Resource
    private DlExamTeacherMapper dlExamTeacherMapper;
    @Resource
    private DlExamRoomMapper dlExamRoomMapper;
    @Resource
    private DlExamPlanMapper mapper;
    @Resource
    private DlExamResultMapper dlExamResultMapper;
    @Resource
    private DlExamStudentMapper dlExamStudentMapper;
    @Resource
    private DlClassTeacherMapper dlClassTeacherMapper;
    @Resource
    private DlExamScoreMapper dlExamScoreMapper;
    @Resource
    private DlExamClassMapper dlExamClassMapper;
    @Resource
    private DlExamSubjectMapper dlExamSubjectMapper;
    @Resource
    private DlStudentMapper dlStudentMapper;
    @Resource
    private DlGradeMapper dlGradeMapper;
    @Resource
    private DlExamListMapper dlExamListMapper;
    @Resource
    private DlExamRoomService dlExamRoomService;



    @Override
    public boolean checkTeacherIsEnough(String examId) {
        LambdaQueryWrapper<DlExamTeacherEntity> teacherQuery = Wrappers.lambdaQuery();
        teacherQuery.eq(DlExamTeacherEntity::getExamId, examId);
        LambdaQueryWrapper<DlExamRoomEntity> roomQuery = Wrappers.lambdaQuery();
        roomQuery.eq(DlExamRoomEntity::getExamId, examId);
        int r_count = dlExamRoomMapper.selectCount(roomQuery);
        int t_count = dlExamTeacherMapper.selectCount(teacherQuery);
        DlExamPlanEntity bean = mapper.selectById(examId);
        int invigilatorNum = 2;
        if(bean.getInvigilatorNum() != null)
            invigilatorNum = bean.getInvigilatorNum();
//        System.out.println("r_count:" + r_count + "   t_count:" + t_count);
        if(r_count * invigilatorNum <= t_count)
            return true;
        return false;
    }

    @Override
    @Transactional
    public void createExamStudentMessageResult(String examId) {
        DlExamPlanEntity exam = mapper.selectById(examId);
        DlGradeEntity grade = dlGradeMapper.selectById(exam.getGradeId());

        //获取所有考场列表
        LambdaQueryWrapper<DlExamRoomEntity> queryRoom = Wrappers.lambdaQuery();
        queryRoom.eq(DlExamRoomEntity::getExamId, examId);
        List<DlExamRoomEntity> room_list = dlExamRoomMapper.selectList(queryRoom);

        //获取考试科目列表
        LambdaQueryWrapper<DlExamSubjectEntity> queryCourse = Wrappers.lambdaQuery();
        queryCourse.eq(DlExamSubjectEntity::getExamId, examId);
        List<DlExamSubjectEntity> course_list = dlExamSubjectMapper.selectList(queryCourse);

        //获取班级列表
        LambdaQueryWrapper<DlExamClassEntity> queryClass = Wrappers.lambdaQuery();
        queryClass.eq(DlExamClassEntity::getExamId, examId);
        List<DlExamClassEntity> class_list = dlExamClassMapper.selectList(queryClass);

        //获得了所有的参考班级的学生信息后，就可以开始给其排考了安排考场，安排考号，安排座位号
        List<DlStudentEntity> stu_list = new ArrayList<>();
        LambdaQueryWrapper<DlStudentEntity> queryStu = Wrappers.lambdaQuery();
        for(DlExamClassEntity cl: class_list){
            queryStu.clear();
            queryStu.eq(DlStudentEntity::getClassId, cl.getClassId());
            List<DlStudentEntity> list = dlStudentMapper.selectList(queryStu);
            stu_list.addAll(list);
        }

        //打乱学生的顺序
        Collections.shuffle(stu_list);
        int count = 0;
        int stu_length = stu_list.size();




        DlExamStudentEntity student = null;
        DlExamScoreEntity scoreEntity = null;
        DlExamResultEntity resultEntity = null;

        if(stu_length == 0){
            return;
        }
        //生成考生座位信息及考好信息
        for(DlExamRoomEntity room: room_list){
            int capacity = room.getCapacity();
            for(int i = 0; i < capacity; i ++){
                //创建result数据
                //exam_plan_id,exam_list_id,exam_room_id,class_id,student_id,学生考号,座位号
                student = new DlExamStudentEntity();
                student.setExamPlanId(examId);
                student.setExamRoomId(room.getId());
                //result.setExamListId();
                student.setClassId(stu_list.get(count).getClassId());
                student.setStudentId(stu_list.get(count).getId());
                student.setStuName(stu_list.get(count).getSName());
                student.setStuNum(stu_list.get(count).getSNo());

                student.setStuSeatNum(i + 1 + "");
                stu_list.get(count).setSeatNum(i + 1 );
                student.setStuExamNum(createExamNumber(grade.getGradeName(), count + 1));
                dlExamStudentMapper.insert(student);
                count ++;
                //当所有学生已经排完时，就结束排考
                if(count >= stu_length)
                    break;
            }
            if(count >= stu_length)
                break;
        }
        //用于保存教师ID信息，key为classId,value为teahcerId
        Map<String, DlExamResultEntity> map = new HashMap<>();
        DlExamRoomEntity roomEntity = null;
        //生成学生成绩列表
        for(DlExamSubjectEntity cou: course_list){

            for(DlExamClassEntity clazz: class_list){
                resultEntity = new DlExamResultEntity();
                resultEntity.setExamId(examId);
                resultEntity.setExamTitle(exam.getName());
                resultEntity.setCourseId(cou.getCourseId());
                resultEntity.setGradeId(exam.getGradeId());
                resultEntity.setInputStatus(2);
                resultEntity.setCourseName(cou.getCourseName());
                resultEntity.setClassId(clazz.getClassId());
                resultEntity.setClassName(clazz.getClassName());
                String teacherId = getTeacherByCourseIDAndClassID(cou.getCourseId(), clazz.getClassId());
                resultEntity.setTeacherId(teacherId);
                resultEntity.setExamTime(cou.getExamDate());
                resultEntity.setStatus(1);
                dlExamResultMapper.insert(resultEntity);
                map.put(clazz.getClassId(), resultEntity);
            }

            for(DlStudentEntity stu: stu_list){
                //创建科目数据，用于后期成绩录入用
                resultEntity = map.get(stu.getClassId());
                scoreEntity = new DlExamScoreEntity();
                scoreEntity.setStudentId(stu.getId());
                scoreEntity.setStudentName(stu.getSName());
                scoreEntity.setTotalScore(cou.getTotalScore());
                scoreEntity.setExamId(examId);
                scoreEntity.setResultId(resultEntity.getId());
                scoreEntity.setCourseId(resultEntity.getCourseId());
                scoreEntity.setSeatNum(stu.getSeatNum());
                roomEntity = dlExamRoomService.getExamRoomBeanByStuId(stu.getId(), examId);
                if(roomEntity != null) {
                    scoreEntity.setExamRoomName(roomEntity.getRoomName());
                    scoreEntity.setExamRoomId(roomEntity.getId());
                }
                dlExamScoreMapper.insert(scoreEntity);
            }

        }
    }

    /**
     * 查询任课教师的ID
     * @param course_id
     * @param class_id
     * @return
     */
    public String getTeacherByCourseIDAndClassID(String course_id, String class_id){
        LambdaQueryWrapper<DlClassTeacherEntity> query = Wrappers.lambdaQuery();
        query.eq(DlClassTeacherEntity::getClassId, class_id);
        query.eq(DlClassTeacherEntity::getCourseId, course_id);
        List<DlClassTeacherEntity> list = dlClassTeacherMapper.selectList(query);
        if(list.size() > 0)
            return list.get(0).getTeacherId();
        return null;
    }

    @Override
    public DLExamPlanVo getPlanVoBeanById(String id) {
        DLExamPlanVo bean = new DLExamPlanVo();
        DlExamPlanEntity exam = mapper.selectById(id);
        //获取所有考场列表
        LambdaQueryWrapper<DlExamRoomEntity> queryRoom = Wrappers.lambdaQuery();
        queryRoom.eq(DlExamRoomEntity::getExamId, id);
        List<DlExamRoomEntity> room_list = dlExamRoomMapper.selectList(queryRoom);

        //获取考试科目列表
        LambdaQueryWrapper<DlExamSubjectEntity> queryCourse = Wrappers.lambdaQuery();
        queryCourse.eq(DlExamSubjectEntity::getExamId, id);
        List<DlExamSubjectEntity> course_list = dlExamSubjectMapper.selectList(queryCourse);

        //获取班级列表
        LambdaQueryWrapper<DlExamClassEntity> queryClass = Wrappers.lambdaQuery();
        queryClass.eq(DlExamClassEntity::getExamId, id);
        List<DlExamClassEntity> class_list = dlExamClassMapper.selectList(queryClass);

        //获取所有监考教师列表
        LambdaQueryWrapper<DlExamTeacherEntity> queryTea = Wrappers.lambdaQuery();
        queryTea.eq(DlExamTeacherEntity::getExamId, id);
        List<DlExamTeacherEntity> tea_list = dlExamTeacherMapper.selectList(queryTea);

        List<DlExamListEntity> list_list = dlExamListMapper.getListByExamId(id);
        for(DlExamListEntity li: list_list){
            for(DlExamRoomEntity room: room_list){
                if(li.getExamRoomId().equals(room.getId()))
                    li.setExamRoomId(room.getRoomName()+ "(" + room.getClassRoomName() + ")");
            }

            for(DlExamSubjectEntity cou: course_list){
                if(cou.getCourseId().equals(li.getCourseId()))
                    li.setCourseId(cou.getCourseName());
            }
        }
        exam.setGradeId(dlGradeMapper.selectById(exam.getGradeId()).getGradeNameLabel());


        bean.setBean(exam);
        bean.setClassList(class_list);
        bean.setCourseList(course_list);
        bean.setRoomList(room_list);
        bean.setTeacherList(tea_list);
        bean.setListList(list_list);
        return bean;
    }


    /**
     * 创建考号
     * @param gradeNum 年级号
     * @param count 考生序号
     * @return
     */
    public String createExamNumber(String gradeNum, int count){
        String examNum = gradeNum;
        if(count / 10 == 0){
            examNum += "000" + count;
        }else if(count / 100 == 0){
            examNum += "00" + count;
        }else if(count / 1000 == 0){
            examNum += "0" + count;
        }else {
            examNum += count;
        }
        return examNum;
    }
}