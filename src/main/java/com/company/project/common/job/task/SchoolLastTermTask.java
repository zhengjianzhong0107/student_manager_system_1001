package com.company.project.common.job.task;

import com.company.project.common.utils.SpringContextUtils;
import com.company.project.entity.DlGradeEntity;
import com.company.project.service.DlGradeService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 上学期开始时，调用的定时任务，修改年级中的grade_type值
 */
@Component("schoolLastTermTask")
public class SchoolLastTermTask implements BaseTask{

    @Override
    public void run(String params){
        DlGradeService dlGradeService = (DlGradeService)SpringContextUtils.getBean("dlGradeService");
        List<DlGradeEntity> list = dlGradeService.getGradeList();

        for (DlGradeEntity bean: list){
            Integer gradeType = bean.getGradeNum();
            if(gradeType != null){
                Integer b = gradeType / 100 % 10;//取百位数，表示几年一更，用于判断是否需要更新
                Integer s = gradeType /10 % 10;//取十位数
                if(s == b){//如果年级数已经等于了更替年数时，就不能再加，而是更新该年级为毕业了
                    bean.setStatus(2);
                    dlGradeService.updateById(bean);
                }else{
                    bean.setGradeNum(gradeType + 10 - 1);
                    dlGradeService.updateById(bean);
                }
            }
        }
    }










}
