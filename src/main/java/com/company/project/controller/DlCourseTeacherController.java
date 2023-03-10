package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import com.company.project.common.utils.DataResult;

import com.company.project.entity.DlCourseTeacherEntity;
import com.company.project.service.DlCourseTeacherService;



/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlCourseTeacherController {
    @Autowired
    private DlCourseTeacherService dlCourseTeacherService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlCourseTeacher")
    public String dlCourseTeacher() {
        return "dlcourseteacher/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlCourseTeacher/add")
    @RequiresPermissions("dlCourseTeacher:add")
    @ResponseBody
    public DataResult add(@RequestBody DlCourseTeacherEntity dlCourseTeacher){
        dlCourseTeacherService.save(dlCourseTeacher);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlCourseTeacher/delete")
    @RequiresPermissions("dlCourseTeacher:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlCourseTeacherService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlCourseTeacher/update")
    @RequiresPermissions("dlCourseTeacher:update")
    @ResponseBody
    public DataResult update(@RequestBody DlCourseTeacherEntity dlCourseTeacher){
        dlCourseTeacherService.updateById(dlCourseTeacher);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlCourseTeacher/listByPage")
    @RequiresPermissions("dlCourseTeacher:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlCourseTeacherEntity dlCourseTeacher){
        Page page = new Page(dlCourseTeacher.getPage(), dlCourseTeacher.getLimit());
        LambdaQueryWrapper<DlCourseTeacherEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlCourseTeacherEntity::getId, dlCourseTeacher.getId());
        IPage<DlCourseTeacherEntity> iPage = dlCourseTeacherService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
