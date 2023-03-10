package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlExamPlanEntity;
import com.company.project.entity.DlExamScoreEntity;
import com.company.project.vo.resp.DlScoreVo;
import com.company.project.vo.resp.DlStudentScoreVo;
import com.company.project.vo.resp.ScoreRangeVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * 
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-07 17:27:40
 */
public interface DlExamScoreMapper extends BaseMapper<DlExamScoreEntity> {
    /**
     * 通过学生ID查询该学生的历史考试成绩,---目前只取了前10条数据
     * @param studentId
     * @return
     */
	List<DlStudentScoreVo> getStuScoreListByStuId(String studentId);

    /**
     * 获取某次考试的成绩 ,  通过传入的参数不通，来查询的数据不一样，可以查询个人成绩（传学生ID），也可以查询班级成绩（只传examId和classId）
     * 如果及没有传学生ID，也没有传classID，那么就表示查询的是整个年级的数据
     * examId是必填项
     * @param bean
     * @return
     */
	List<DlStudentScoreVo> getStuScoreByConditions(DlExamScoreEntity bean);

    /**
     * 统计班级中的各科的平均成绩，分数保存在score字段中，（数据只有课程ID，课程名称，平均分数）
     * 必传参数examId
     * 选传参数classId
     * 如果只传了examId就是查询的年级平均分数
     * 如果传了classId就是查询班级平均分数
     * @param bean
     * @return
     */
    List<DlExamScoreEntity> getAvgScoreByConditions(DlExamScoreEntity bean);

    /**
     * 查询学生考试的成绩列表信息
     * examId和studentId必传，也只要这两个参数值
     * @param bean
     * @return
     */
	List<DlScoreVo> getStuExamScore(DlExamScoreEntity bean);

    /**
     * 查询排好序的成绩列表
     * examId 和 courseId必传
     * classId可传，可以不传，如果传了，表示查询班级某科目的成绩排名情况，如果没有传，表示查询的是年级排名
     *
     * @param bean
     * @return
     */
	List<DlExamScoreEntity> getOrderList(DlExamScoreEntity bean);

    /**
     * 查询总分分数列表
     * @param bean
     * @return
     */
	List<BigDecimal> getSumScoreOrderList(DlExamScoreEntity bean);

    /**
     * 查询上一次考试的ID
     * @param bean 需要传两个参数，当前考试的起始时间，和学生ID 学生ID存放到bean中的id属性中
     * @return
     */
    String getUpExamId(DlExamPlanEntity bean);


    /**
     * 查询年级的不同科目所有分段的的学生人数，以及平均分
     * @param examId
     * @return
     */
    List<ScoreRangeVO> getExamGradeRangeScoreByExamId(String examId);

    /**
     * 查询年级中参考班级的不同科目的所有分段的学生人数，以及平均分
     * @param examId
     * @return
     */
    List<ScoreRangeVO> getExamGradeClassRangeScoreByExamId(String examId);

    /**
     * 查询指定年级中参考班级的不同科目的所有分段的学生人数，以及平均分
     * @param bean
     * @return
     */
    List<ScoreRangeVO> getExamClassRangeScoreByExamIdAndClassId(DlExamScoreEntity bean);

}
