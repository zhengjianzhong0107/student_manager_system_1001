package com.company.project.vo.resp;

import com.company.project.entity.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class DLExamPlanVo {
    private DlExamPlanEntity bean;//考试基本设置信息
    private List<DlExamSubjectEntity> courseList;//参考科目列表
    private List<DlExamRoomEntity> roomList;//考场列表
    private List<DlExamTeacherEntity> teacherList;//监考教师列表
    private List<DlExamClassEntity> classList;//参考班级列表
    private List<DlExamListEntity> listList;//考试安排列表
}
