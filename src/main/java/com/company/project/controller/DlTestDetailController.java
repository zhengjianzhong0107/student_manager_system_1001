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

import com.company.project.entity.DlTestDetailEntity;
import com.company.project.service.DlTestDetailService;



/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlTestDetailController {
    @Autowired
    private DlTestDetailService dlTestDetailService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlTestDetail")
    public String dlTestDetail() {
        return "dltestdetail/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlTestDetail/add")
    @RequiresPermissions("dlTestDetail:add")
    @ResponseBody
    public DataResult add(@RequestBody DlTestDetailEntity dlTestDetail){
        dlTestDetailService.save(dlTestDetail);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlTestDetail/delete")
    @RequiresPermissions("dlTestDetail:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlTestDetailService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlTestDetail/update")
    @RequiresPermissions("dlTestDetail:update")
    @ResponseBody
    public DataResult update(@RequestBody DlTestDetailEntity dlTestDetail){
        dlTestDetailService.updateById(dlTestDetail);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlTestDetail/listByPage")
    @RequiresPermissions("dlTestDetail:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlTestDetailEntity dlTestDetail){
        Page page = new Page(dlTestDetail.getPage(), dlTestDetail.getLimit());
        LambdaQueryWrapper<DlTestDetailEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlTestDetailEntity::getId, dlTestDetail.getId());
        IPage<DlTestDetailEntity> iPage = dlTestDetailService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
