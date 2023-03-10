package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamResultEntity;
import com.company.project.service.DlExamPlanService;
import com.company.project.service.DlExamResultService;
import com.company.project.service.HttpSessionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:46
 */
@Controller
@RequestMapping("/")
public class DlExamResultController {
    @Autowired
    private DlExamResultService dlExamResultService;
    @Autowired
    private HttpSessionService httpSessionService;
    @Autowired
    private DlExamPlanService dlExamPlanService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamResult")
    public String dlExamResult() {
        return "dlexamresult/list";
    }

    @GetMapping("/index/dlExamResultInput")
    public String input(){
        return "dlexamresult/input";
    }

    @GetMapping("/index/dlExamResultUnifyInput")
    public String unifyInput(){
        return "dlexamresult/unify_input";
    }


    @ApiOperation(value = "新增")
    @PostMapping("dlExamResult/add")
    @RequiresPermissions("dlExamResult:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamResultEntity dlExamResult){
        dlExamResultService.save(dlExamResult);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamResult/delete")
    @RequiresPermissions("dlExamResult:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamResultService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamResult/update")
    @RequiresPermissions("dlExamResult:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamResultEntity dlExamResult){
        dlExamResultService.updateById(dlExamResult);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamResult/listByPage")
    @RequiresPermissions("dlExamResult:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamResultEntity dlExamResult){
        Page page = new Page(dlExamResult.getPage(), dlExamResult.getLimit());
        LambdaQueryWrapper<DlExamResultEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlExamResultEntity::getId, dlExamResult.getId());
        IPage<DlExamResultEntity> iPage = dlExamResultService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamResult/myList")
    @RequiresPermissions("dlExamResult:list")
    @ResponseBody
    public DataResult list(@RequestBody DlExamResultEntity dlExamResult){
        Page page = new Page(dlExamResult.getPage(), dlExamResult.getLimit());
        LambdaQueryWrapper<DlExamResultEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.eq(DlExamResultEntity::getTeacherId, httpSessionService.getCurrentUserId());
        queryWrapper.eq(DlExamResultEntity::getInputStatus, 1);

        IPage<DlExamResultEntity> iPage = dlExamResultService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamResult/classList")
    @RequiresPermissions("dlExamResult:list")
    @ResponseBody
    public DataResult classList(@RequestBody DlExamResultEntity dlExamResult){
        Page page = new Page(dlExamResult.getPage(), dlExamResult.getLimit());
        LambdaQueryWrapper<DlExamResultEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlExamResult.getExamTitle())){
            queryWrapper.like(DlExamResultEntity::getExamTitle, dlExamResult.getExamTitle());
        }
        if(!StringUtils.isEmpty(dlExamResult.getGradeId())){
            queryWrapper.eq(DlExamResultEntity::getGradeId, dlExamResult.getGradeId());
        }
        if(!StringUtils.isEmpty(dlExamResult.getClassId())){
            queryWrapper.eq(DlExamResultEntity::getClassId, dlExamResult.getClassId());
        }

        queryWrapper.groupBy(DlExamResultEntity::getExamId, DlExamResultEntity::getClassId);

        IPage<DlExamResultEntity> iPage = dlExamResultService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


}
