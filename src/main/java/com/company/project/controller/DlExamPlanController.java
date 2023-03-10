package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.service.*;
import com.company.project.vo.resp.DLExamPlanVo;
import com.company.project.vo.resp.DlTransferVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;



/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlExamPlanController {
    @Autowired
    private DlExamPlanService dlExamPlanService;
    @Resource
    private DlClassService dlClassService;
    @Resource
    private DlExamClassService dlExamClassService;
    @Resource
    private DlCourseService dlCourseService;
    @Resource
    private DlExamSubjectService dlExamSubjectService;
    @Resource
    private DlGradeService dlGradeService;
    @Autowired
    private DlExamResultService dlExamResultService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamPlan")
    public String dlExamPlan() {
        return "dlexamplan/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamPlan/add")
    @RequiresPermissions("dlExamPlan:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamPlanEntity dlExamPlan){
        dlExamPlan.setStatus(1);
        dlExamPlan.setResultInputStatus(2);//不能考试录入
        dlExamPlanService.save(dlExamPlan);
        return DataResult.success(dlExamPlan);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamPlan/delete")
    @RequiresPermissions("dlExamPlan:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamPlanService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamPlan/update")
    @RequiresPermissions("dlExamPlan:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamPlanEntity dlExamPlan){
        if(dlExamPlan.getResultInputStatus() != null){//如果更新了成绩录入状态，那么就需要去更改该考试下面的成绩录入数据状态
            LambdaQueryWrapper<DlExamResultEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(DlExamResultEntity::getExamId, dlExamPlan.getId());
            List<DlExamResultEntity> list = dlExamResultService.list(queryWrapper);
            for(DlExamResultEntity bean: list){
                bean.setInputStatus(dlExamPlan.getResultInputStatus());
                dlExamResultService.updateById(bean);
            }
        }
        dlExamPlanService.updateById(dlExamPlan);
        return DataResult.success(dlExamPlan);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamPlan/listByPage")
    @RequiresPermissions("dlExamPlan:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamPlanEntity dlExamPlan){
        Page page = new Page(dlExamPlan.getPage(), dlExamPlan.getLimit());
        LambdaQueryWrapper<DlExamPlanEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlExamPlan.getName())){
            queryWrapper.like(DlExamPlanEntity::getName, dlExamPlan.getName());
        }
        if(dlExamPlan.getStatus() != null){
            queryWrapper.ge(DlExamPlanEntity::getStatus, dlExamPlan.getStatus());
        }
        IPage<DlExamPlanEntity> iPage = dlExamPlanService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }





    @ApiOperation(value = "查询参考科目列表")
    @PostMapping("dlExamPlan/courseVoListByGradeId")
    @RequiresPermissions("dlExamPlan:list")
    @ResponseBody
    public DataResult getListClassVoByGradeId(@RequestBody DlExamPlanEntity dlPlan){
        LambdaQueryWrapper<DlCourseEntity> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isEmpty(dlPlan.getGradeId())){
            return DataResult.fail("未传入参数，请重新再试");
        }

        DlGradeEntity gradeEntity = dlGradeService.getById(dlPlan.getGradeId());
        if(gradeEntity == null){
            return DataResult.fail("未找到相应的数据");
        }
        DlTransferVo vo = new DlTransferVo();
        queryWrapper.eq(DlCourseEntity::getGrade, gradeEntity.getGradeNum());
        List<DlCourseEntity> list = dlCourseService.list(queryWrapper);
        vo.setBeans(list);
        LambdaQueryWrapper<DlExamSubjectEntity> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.eq(DlExamSubjectEntity::getExamId, dlPlan.getId());
        List<DlExamSubjectEntity> valueList = dlExamSubjectService.list(queryWrapper1);
        List<String> ids = new ArrayList<>();
        for(DlExamSubjectEntity bean: valueList){
            ids.add(bean.getCourseId());
        }
        vo.setValues(ids);
        return DataResult.success(vo);
    }

    @ApiOperation(value = "查询班级列表")
    @PostMapping("dlExamPlan/classVoListByGradeId")
    @RequiresPermissions("dlExamPlan:list")
    @ResponseBody
    public DataResult getListByGradeId(@RequestBody DlExamPlanEntity dlPlan){
        dlPlan = dlExamPlanService.getById(dlPlan.getId());
        LambdaQueryWrapper<DlClassEntity> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isEmpty(dlPlan.getGradeId())){
            return DataResult.fail("未传入参数，请重新再试");
        }
        DlTransferVo vo = new DlTransferVo();
        queryWrapper.eq(DlClassEntity::getGradeId, dlPlan.getGradeId());
        List<DlClassEntity> list = dlClassService.list(queryWrapper);
        vo.setBeans(list);
        LambdaQueryWrapper<DlExamClassEntity> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.eq(DlExamClassEntity::getExamId, dlPlan.getId());
        List<DlExamClassEntity> valueList = dlExamClassService.list(queryWrapper1);
        List<String> ids = new ArrayList<>();
        for(DlExamClassEntity bean: valueList){
            ids.add(bean.getClassId());
        }
        vo.setValues(ids);
        return DataResult.success(vo);
    }


    @ApiOperation(value = "检查监考教师人数是否够")
    @PostMapping("dlExamPlan/checkTeacherIsEnough")
    @RequiresPermissions("dlExamPlan:list")
    @ResponseBody
    public DataResult checkTeacherIsEnough(@RequestBody DlExamPlanEntity dlPlan){
        return DataResult.success(dlExamPlanService.checkTeacherIsEnough(dlPlan.getId()));
    }


    /**
     *
     * @param dlPlan
     * @return
     */
    @ApiOperation(value = "确认并发布考试计划")
    @PostMapping("dlExamPlan/sureAndRelease")
    @RequiresPermissions("dlExamPlan:add")
    @ResponseBody
    public DataResult sureAndReleaseExamPlan(@RequestBody DlExamPlanEntity dlPlan){
        dlPlan.setStatus(2);
        dlExamPlanService.updateById(dlPlan);
        //创建学生信息
        dlExamPlanService.createExamStudentMessageResult(dlPlan.getId());
        return DataResult.success();
    }

    @ApiOperation(value = "考试计划详情")
    @GetMapping("dlExamPlan/details")
    @RequiresPermissions("dlExamPlan:details")
    public String examDetails(String id, Model model){
        DLExamPlanVo vo = dlExamPlanService.getPlanVoBeanById(id);
        model.addAttribute("plan", vo);
        return "dlexamplan/details";
    }


    @ApiOperation(value = "查询可以录入成绩的考试数据")
    @PostMapping("dlExamPlan/inputList")
    @RequiresPermissions("dlExamPlan:list")
    @ResponseBody
    public DataResult getExamList(@RequestBody DlExamPlanEntity bean){
        Page page = new Page(bean.getPage(), bean.getLimit());
        LambdaQueryWrapper<DlExamPlanEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.eq(DlExamPlanEntity::getResultInputStatus, 1);
        IPage<DlExamPlanEntity> iPage = dlExamPlanService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }



}
