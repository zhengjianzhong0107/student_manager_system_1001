package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrBaseEntity;
import com.company.project.service.DlArrBaseService;
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
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-21 14:29:57
 */
@Controller
@RequestMapping("/")
public class DlArrBaseController {
    @Autowired
    private DlArrBaseService dlArrBaseService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlArrBase")
    public String dlArrBase() {
        return "dlarrbase/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlArrBase/add")
    @RequiresPermissions("dlArrBase:add")
    @ResponseBody
    public DataResult add(@RequestBody DlArrBaseEntity dlArrBase){
        dlArrBaseService.save(dlArrBase);
        return DataResult.success();
    }


    @ApiOperation(value = "添加所有的课程信息")
    @PostMapping("dlArrBase/addTcs")
    @RequiresPermissions("dlArrBase:add")
    @ResponseBody
    public DataResult addTcs(@RequestBody DlArrBaseEntity dlArrBase){
        if(StringUtils.isEmpty(dlArrBase.getArrId())){
            return DataResult.fail("未接收到排课任务ID");
        }

        //添加所有的课程信息，到需要排课的信息中
        return dlArrBaseService.copyClassTeacherToThisBean(dlArrBase.getArrId());
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlArrBase/delete")
    @RequiresPermissions("dlArrBase:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlArrBaseService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlArrBase/update")
    @RequiresPermissions("dlArrBase:update")
    @ResponseBody
    public DataResult update(@RequestBody DlArrBaseEntity dlArrBase){
        dlArrBaseService.updateById(dlArrBase);
        return DataResult.success();
    }


    @ApiOperation(value = "查询参排教师列表")
    @PostMapping("dlArrBase/teachers")
    @RequiresPermissions("dlArrBase:list")
    @ResponseBody
    public DataResult getTeacherList(@RequestBody DlArrBaseEntity dlArrBase){
        return dlArrBaseService.getTeacherList(dlArrBase);
    }


    @ApiOperation(value = "查询参排课程列表")
    @PostMapping("dlArrBase/courses")
    @RequiresPermissions("dlArrBase:list")
    @ResponseBody
    public DataResult getCourseList(@RequestBody DlArrBaseEntity dlArrBase){


        return dlArrBaseService.getCourseList(dlArrBase);
    }

    @ApiOperation(value = "查询参排课程列表")
    @PostMapping("dlArrBase/classes")
    @RequiresPermissions("dlArrBase:list")
    @ResponseBody
    public DataResult getClassList(@RequestBody DlArrBaseEntity dlArrBase){
        return dlArrBaseService.getClassList(dlArrBase);
    }



    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlArrBase/listByPage")
    @RequiresPermissions("dlArrBase:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlArrBaseEntity dlArrBase){
        Page page = new Page(dlArrBase.getPage(), dlArrBase.getLimit());
        LambdaQueryWrapper<DlArrBaseEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例

        queryWrapper.eq(DlArrBaseEntity::getArrId, dlArrBase.getArrId());
        System.out.println(dlArrBase);
        if(org.apache.commons.lang.StringUtils.isNotEmpty(dlArrBase.getCourseName())){
            queryWrapper.and(i -> i.like(DlArrBaseEntity::getCourseName, dlArrBase.getCourseName())
                    .or().like(DlArrBaseEntity::getClassName, dlArrBase.getCourseName())
                    .or().like(DlArrBaseEntity::getTeacherName, dlArrBase.getCourseName()));
        }

        IPage<DlArrBaseEntity> iPage = dlArrBaseService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
