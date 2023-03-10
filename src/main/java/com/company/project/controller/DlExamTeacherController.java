package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamTeacherEntity;
import com.company.project.service.DlExamTeacherService;
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
 * @date 2020-12-28 13:09:20
 */
@Controller
@RequestMapping("/")
public class DlExamTeacherController {
    @Autowired
    private DlExamTeacherService dlExamTeacherService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamTeacher")
    public String dlExamTeacher() {
        return "dlexamteacher/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamTeacher/add")
    @RequiresPermissions("dlExamTeacher:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamTeacherEntity dlExamTeacher){
        if(StringUtils.isEmpty(dlExamTeacher.getExamId())){
            return DataResult.fail("考试计划主键缺失，请退出重新再试");
        }
        LambdaQueryWrapper<DlExamTeacherEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamTeacherEntity::getExamId, dlExamTeacher.getExamId());
        queryWrapper.eq(DlExamTeacherEntity::getTeacherId, dlExamTeacher.getTeacherId());
        int count = dlExamTeacherService.count(queryWrapper);
        if(count > 0){
            return DataResult.fail("已经添加该教师了，请重新选择");
        }

        dlExamTeacherService.save(dlExamTeacher);
        return DataResult.success();
    }

    @ApiOperation(value = "快速添加监考教师")
    @PostMapping("dlExamTeacher/addTeachers")
    @RequiresPermissions("dlExamTeacher:add")
    @ResponseBody
    public DataResult fastAddTeachers(@RequestBody DlExamTeacherEntity dlExamTeacher){

        dlExamTeacherService.fastAddTeachers(dlExamTeacher.getExamId());
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamTeacher/delete")
    @RequiresPermissions("dlExamTeacher:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamTeacherService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamTeacher/update")
    @RequiresPermissions("dlExamTeacher:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamTeacherEntity dlExamTeacher){
        dlExamTeacherService.updateById(dlExamTeacher);
        return DataResult.success();
    }



    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamTeacher/listByPage")
    @RequiresPermissions("dlExamTeacher:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamTeacherEntity dlExamTeacher){
        Page page = new Page(dlExamTeacher.getPage(), dlExamTeacher.getLimit());
        LambdaQueryWrapper<DlExamTeacherEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.eq(DlExamTeacherEntity::getExamId, dlExamTeacher.getExamId());

        IPage<DlExamTeacherEntity> iPage = dlExamTeacherService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamTeacher/getListByExamId")
    @RequiresPermissions("dlExamTeacher:list")
    @ResponseBody
    public DataResult getListByExamId(@RequestBody DlExamTeacherEntity bean){
        return DataResult.success(dlExamTeacherService.getListByExamId(bean.getExamId()));
    }

}
