package com.company.project.common.job.task;

import com.company.project.common.utils.SpringContextUtils;
import com.company.project.entity.DlGradeEntity;
import com.company.project.service.DlGradeService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 下学期开始时，调用的定时任务，修改年级中的grade_type值
 *
 * @author: YangHui
 * @version V1.0
 * @date: 2021/2/18
 */
@Component("schoolNextTermTask")
public class SchoolNextTermTask implements BaseTask{


    @Override
    public void run(String params){
        DlGradeService dlGradeService = (DlGradeService) SpringContextUtils.getBean("dlGradeService");
        List<DlGradeEntity> list = dlGradeService.getGradeList();

        for (DlGradeEntity bean: list){
            Integer gradeType = bean.getGradeNum();
            if(gradeType != null){
               bean.setGradeNum(gradeType + 1);
               dlGradeService.updateById(bean);
            }
        }
    }









}
