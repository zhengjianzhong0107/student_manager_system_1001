package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamScoreEntity;
import com.company.project.service.DlExamScoreService;
import com.company.project.service.DlStudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-07 17:27:40
 */
@Controller
@RequestMapping("/")
public class DlExamScoreController {
    @Autowired
    private DlExamScoreService dlExamScoreService;
    @Autowired
    private DlStudentService dlStudentService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamScore")
    public String dlExamScore() {
        return "dlexamscore/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamScore/add")
    @RequiresPermissions("dlExamScore:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamScoreEntity dlExamScore){
        dlExamScoreService.save(dlExamScore);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamScore/delete")
    @RequiresPermissions("dlExamScore:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamScoreService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamScore/update")
    @RequiresPermissions("dlExamScore:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamScoreEntity dlExamScore){
        dlExamScoreService.updateById(dlExamScore);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamScore/listByPage")
    @RequiresPermissions("dlExamScore:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamScoreEntity dlExamScore){
        Page page = new Page(dlExamScore.getPage(), dlExamScore.getLimit());
        LambdaQueryWrapper<DlExamScoreEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlExamScore.getResultId()))
            queryWrapper.eq(DlExamScoreEntity::getResultId, dlExamScore.getResultId());
        if(!StringUtils.isEmpty(dlExamScore.getExamId()))
            queryWrapper.eq(DlExamScoreEntity::getExamId, dlExamScore.getExamId());
        if(!StringUtils.isEmpty(dlExamScore.getCourseId())){
            queryWrapper.eq(DlExamScoreEntity::getCourseId, dlExamScore.getCourseId());
        }
        queryWrapper.orderByAsc(DlExamScoreEntity::getExamRoomName, DlExamScoreEntity::getSeatNum);


        IPage<DlExamScoreEntity> iPage = dlExamScoreService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


    @ApiOperation(value = "查询学生成绩统计报告信息")
    @GetMapping("dlExamScore/stuScoreDetails")
    @RequiresPermissions("dlExamScore:list")
    public String studentScoreDetails(String studentId, Model model){
        Map<String, Object> map = dlExamScoreService.getStudentDetailsByStuId(studentId);

        map.put("classType", dlStudentService.getClassTypeByStudentId(studentId));
        model.addAttribute("data", map);
        return "dlexamscore/stu_score_details";
    }


    /**
     * 需要统计出各科的平均成绩和学生成绩，同时还需要查询上次学生考试成绩，进行对比分析，科目之间也需要进行对比分析，有没有出现偏科情况
     * @param examId
     * @param studentId
     * @param model
     * @return
     */
    @ApiOperation(value = "查看学生考试分析报告")
    @GetMapping("dlExamScore/examAnalysisReport")
    @RequiresPermissions("dlExamScore:list")
    public String stuAnalysisReport(String examId, String studentId, Model model){
        DlExamScoreEntity bean = new DlExamScoreEntity();
        bean.setExamId(examId);
        bean.setStudentId(studentId);
        model.addAttribute("data", dlExamScoreService.getReportByExamIdAndStudentId(bean));
        return "dlexamscore/stu_exam_report";
    }




    @ApiOperation(value = "查看年级学生考试分析报告")
    @GetMapping("dlExamScore/gradeScoreReport")
    @RequiresPermissions("dlExamScore:list")
    public String gradeAnalysisReport(String examId, Model model){
        model.addAttribute("data", dlExamScoreService.getGradeReportDataByExamId(examId));
        return "dlexamscore/grade_exam_report";
    }

    @ApiOperation(value = "查看班级学生考试分析报告")
    @GetMapping("dlExamScore/classScoreReport")
    @RequiresPermissions("dlExamScore:list")
    public String classAnalysisReport(String examId, String classId, Model model){
        model.addAttribute("data",dlExamScoreService.getClassReportDataByExamIdAndClassId(examId, classId));
        return "dlexamscore/class_exam_report";
    }
}
