package com.company.project.controller;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.ExcelUtils;
import com.company.project.entity.DlExamListEntity;
import com.company.project.entity.DlExamListTeacher;
import com.company.project.entity.DlExamPlanEntity;
import com.company.project.service.DlExamListService;
import com.company.project.service.DlExamPlanService;
import com.company.project.service.HttpSessionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
public class DlExamListController {
    @Autowired
    private DlExamListService dlExamListService;
    @Autowired
    private DlExamPlanService dlExamPlanService;
    @Autowired
    private HttpSessionService sessionService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamList")
    public String dlExamList() {
        return "dlexamlist/list";
        }

    /**
     * 跳转到页面
     */
    @GetMapping("/index/dlExamMyList")
    public String myList() {
        return "dlexamlist/my";
    }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamList/add")
    @RequiresPermissions("dlExamList:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamListEntity dlExamList){
        dlExamListService.save(dlExamList);
        return DataResult.success();
    }

    @ApiOperation(value = "新增监考教师")
    @PostMapping("dlExamList/addTeacher")
    @RequiresPermissions("dlExamList:add")
    @ResponseBody
    public DataResult addTeacher(@RequestBody DlExamListTeacher bean){
        dlExamListService.saveTeacher(bean);
        return DataResult.success();
    }



    @ApiOperation(value = "删除监考教师")
    @DeleteMapping("dlExamList/deleteTeacher")
    @RequiresPermissions("dlExamList:delete")
    @ResponseBody
    public DataResult delete(@RequestBody String id){
        dlExamListService.removeListTeacherByTeacherId(id);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamList/delete")
    @RequiresPermissions("dlExamList:delete")
    @ResponseBody
    public DataResult deleteTeacher(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamListService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamList/update")
    @RequiresPermissions("dlExamList:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamListEntity dlExamList){
        dlExamListService.updateById(dlExamList);
        return DataResult.success();
    }


    @ApiOperation(value = "更新")
    @PutMapping("dlExamList/updateTeacher")
    @RequiresPermissions("dlExamList:update")
    @ResponseBody
    public DataResult updateTeacher(@RequestBody DlExamListTeacher bean){
        System.out.println(bean);
        if(StringUtils.isEmpty(bean.getId())){
            return DataResult.fail("主键缺失");
        }
        dlExamListService.updateTeacher(bean);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamList/listByPage")
    @RequiresPermissions("dlExamList:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamListEntity dlExamList){
//        Page page = new Page(dlExamList.getPage(), dlExamList.getLimit());
        List<DlExamListEntity> list = dlExamListService.getListByExamId(dlExamList);
        return DataResult.success(list);
    }

    @ApiOperation(value = "智能快速安排考试任务")
    @PostMapping("dlExamList/fastAdd")
    @RequiresPermissions("dlExamList:add")
    @ResponseBody
    public DataResult fastAdd(@RequestBody DlExamListEntity dlExamList){
        dlExamListService.removeListByExamId(dlExamList.getExamPlanId());
        dlExamListService.fastAdd(dlExamList.getExamPlanId());
        return DataResult.success();
    }

    @ApiOperation(value = "导出考试计划的考试安排信息")
    @RequestMapping("dlExamList/export")
    @RequiresPermissions("dlExamList:list")
    public void exportExamPlanList(@RequestParam String examId, HttpServletResponse response){
        DlExamPlanEntity plan = dlExamPlanService.getById(examId);
        List<DlExamListEntity> list = dlExamListService.getExportListByExamId(examId);
        ExcelUtils.exportExamPlan(list, plan.getName(), response);
    }

    @ApiOperation(value = "获取当前我的考务信息")
    @PostMapping("dlExamList/myList")
    @RequiresPermissions("dlExamList:list")
    @ResponseBody
    public DataResult myExamPlanList(@RequestBody DlExamListEntity bean){
        bean.setTeacherId(sessionService.getCurrentUserId());
        return DataResult.success(dlExamListService.getMyExamPlanList(bean));
    }


    @ApiOperation(value = "通过考务ID查询下面的教师列表信息")
    @PostMapping("dlExamList/teacherList")
    @RequiresPermissions("dlExamList:list")
    @ResponseBody
    public DataResult listTeachers(@RequestBody DlExamListEntity bean){
        return DataResult.success(dlExamListService.getListTeachersByListId(bean.getId()));
    }




}
