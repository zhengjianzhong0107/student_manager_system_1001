package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.NumberUtils;
import com.company.project.entity.DlTestEntity;
import com.company.project.service.DlTestService;
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
public class DlTestController {
    @Autowired
    private DlTestService dlTestService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlTest")
    public String dlTest() {
        return "dltest/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlTest/add")
    @RequiresPermissions("dlTest:add")
    @ResponseBody
    public DataResult add(@RequestBody DlTestEntity dlTest){

        dlTest.setCode(getCode());
        dlTestService.save(dlTest);
        return DataResult.success();
    }

    public String getCode(){
        String code = "";
        while(true){
            code = NumberUtils.getNumCode("SJ");
            LambdaQueryWrapper<DlTestEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(DlTestEntity::getCode, code);
            int count = dlTestService.count(queryWrapper);
            if(count == 0){
                break;
            }
        }
        return code;
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlTest/delete")
    @RequiresPermissions("dlTest:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlTestService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlTest/update")
    @RequiresPermissions("dlTest:update")
    @ResponseBody
    public DataResult update(@RequestBody DlTestEntity dlTest){
        dlTestService.updateById(dlTest);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlTest/listByPage")
    @RequiresPermissions("dlTest:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlTestEntity dlTest){
        Page page = new Page(dlTest.getPage(), dlTest.getLimit());
        LambdaQueryWrapper<DlTestEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlTestEntity::getId, dlTest.getId());
        IPage<DlTestEntity> iPage = dlTestService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
