package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlTermEntity;
import com.company.project.service.DlTermService;
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
 * @date 2021-01-06 14:13:43
 */
@Controller
@RequestMapping("/")
public class DlTermController {
    @Autowired
    private DlTermService dlTermService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlTerm")
    public String dlTerm() {
        return "dlterm/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlTerm/add")
    @RequiresPermissions("dlTerm:add")
    @ResponseBody
    public DataResult add(@RequestBody DlTermEntity dlTerm){
        dlTerm.setStatus(1);
        dlTermService.save(dlTerm);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlTerm/delete")
    @RequiresPermissions("dlTerm:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlTermService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlTerm/update")
    @RequiresPermissions("dlTerm:update")
    @ResponseBody
    public DataResult update(@RequestBody DlTermEntity dlTerm){
        dlTermService.updateById(dlTerm);
        return DataResult.success();
    }



    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlTerm/listByPage")
    @RequiresPermissions("dlTerm:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlTermEntity dlTerm){
        Page page = new Page(dlTerm.getPage(), dlTerm.getLimit());
        LambdaQueryWrapper<DlTermEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlTerm.getTitle()))
            queryWrapper.like(DlTermEntity::getTitle, dlTerm.getTitle());
        queryWrapper.orderByDesc(true, DlTermEntity::getStartTime);
        IPage<DlTermEntity> iPage = dlTermService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
