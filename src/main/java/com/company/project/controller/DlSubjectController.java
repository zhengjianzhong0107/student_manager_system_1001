package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlSubjectEntity;
import com.company.project.service.DlSubjectService;
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
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlSubjectController {
    @Autowired
    private DlSubjectService dlSubjectService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlSubject")
    public String dlSubject() {
        return "dlsubject/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlSubject/add")
    @RequiresPermissions("dlSubject:add")
    @ResponseBody
    public DataResult add(@RequestBody DlSubjectEntity dlSubject){
        dlSubjectService.save(dlSubject);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlSubject/delete")
    @RequiresPermissions("dlSubject:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlSubjectService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlSubject/update")
    @RequiresPermissions("dlSubject:update")
    @ResponseBody
    public DataResult update(@RequestBody DlSubjectEntity dlSubject){
        dlSubjectService.updateById(dlSubject);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlSubject/listByPage")
    @RequiresPermissions("dlSubject:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlSubjectEntity dlSubject){
        Page page = new Page(dlSubject.getPage(), dlSubject.getLimit());
        LambdaQueryWrapper<DlSubjectEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlSubject.getCourseName()))
            queryWrapper.like(DlSubjectEntity::getCourseName, dlSubject.getCourseName());
        if(dlSubject.getClassType() != null){
            queryWrapper.eq(DlSubjectEntity::getClassType, dlSubject.getClassType());
        }
        IPage<DlSubjectEntity> iPage = dlSubjectService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "班级ID查询课程列表")
    @PostMapping("dlSubject/getListByClassType")
    @RequiresPermissions("dlSubject:list")
    @ResponseBody
    public DataResult getListByClassType(@RequestBody DlSubjectEntity bean){
        List<DlSubjectEntity> list = dlSubjectService.getListByClassType(bean.getClassType());
        if(list == null){
            return DataResult.fail("操作有误，请按正常情况操作");
        }
        return DataResult.success(list);
    }

}
