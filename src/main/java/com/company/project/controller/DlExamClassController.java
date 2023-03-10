package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamClassEntity;
import com.company.project.service.DlExamClassService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class DlExamClassController {
    @Autowired
    private DlExamClassService dlExamClassService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamClass")
    public String dlExamClass() {
        return "dlexamclass/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamClass/add")
    @RequiresPermissions("dlExamClass:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamClassEntity dlExamClass){
        dlExamClassService.save(dlExamClass);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamClass/delete")
    @RequiresPermissions("dlExamClass:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamClassService.removeByIds(ids);
        return DataResult.success();
    }


    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamClass/deleteByClassId")
    @RequiresPermissions("dlExamClass:delete")
    @ResponseBody
    public DataResult deleteByClassId(@RequestBody DlExamClassEntity dlExamClass){
        LambdaQueryWrapper<DlExamClassEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamClassEntity::getClassId, dlExamClass.getClassId());
        queryWrapper.eq(DlExamClassEntity::getExamId, dlExamClass.getExamId());
        dlExamClass = dlExamClassService.getOne(queryWrapper);
        if(dlExamClass == null){
            return DataResult.fail("未找到相应的数据");
        }
        dlExamClassService.removeById(dlExamClass.getId());
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamClass/update")
    @RequiresPermissions("dlExamClass:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamClassEntity dlExamClass){
        dlExamClassService.updateById(dlExamClass);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamClass/listByPage")
    @RequiresPermissions("dlExamClass:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamClassEntity dlExamClass){
        Page page = new Page(dlExamClass.getPage(), dlExamClass.getLimit());
        LambdaQueryWrapper<DlExamClassEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlExamClassEntity::getId, dlExamClass.getId());
        IPage<DlExamClassEntity> iPage = dlExamClassService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
