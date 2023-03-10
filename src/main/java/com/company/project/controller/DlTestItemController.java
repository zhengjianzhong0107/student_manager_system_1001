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

import com.company.project.entity.DlTestItemEntity;
import com.company.project.service.DlTestItemService;



/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlTestItemController {
    @Autowired
    private DlTestItemService dlTestItemService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlTestItem")
    public String dlTestItem() {
        return "dltestitem/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlTestItem/add")
    @RequiresPermissions("dlTestItem:add")
    @ResponseBody
    public DataResult add(@RequestBody DlTestItemEntity dlTestItem){
        dlTestItemService.save(dlTestItem);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlTestItem/delete")
    @RequiresPermissions("dlTestItem:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlTestItemService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlTestItem/update")
    @RequiresPermissions("dlTestItem:update")
    @ResponseBody
    public DataResult update(@RequestBody DlTestItemEntity dlTestItem){
        dlTestItemService.updateById(dlTestItem);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlTestItem/listByPage")
    @RequiresPermissions("dlTestItem:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlTestItemEntity dlTestItem){
        Page page = new Page(dlTestItem.getPage(), dlTestItem.getLimit());
        LambdaQueryWrapper<DlTestItemEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlTestItemEntity::getId, dlTestItem.getId());
        IPage<DlTestItemEntity> iPage = dlTestItemService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
