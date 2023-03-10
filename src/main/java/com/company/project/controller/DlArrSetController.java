package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrSetEntity;
import com.company.project.service.DlArrSetService;
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
 * @date 2021-01-19 15:22:38
 */
@Controller
@RequestMapping("/")
public class DlArrSetController {
    @Autowired
    private DlArrSetService dlArrSetService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlArrSet")
    public String dlArrSet() {
        return "dlarrset/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlArrSet/add")
    @RequiresPermissions("dlArrSet:add")
    @ResponseBody
    public DataResult add(@RequestBody DlArrSetEntity dlArrSet){
        dlArrSetService.save(dlArrSet);
        return DataResult.success(dlArrSet);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlArrSet/delete")
    @RequiresPermissions("dlArrSet:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlArrSetService.removeByIds(ids);
        return DataResult.success();
    }



    @ApiOperation(value = "检查是否已经超过了课程数")
    @PostMapping("dlArrSet/isOutOfNum")
    @RequiresPermissions("dlArrSet:list")
    @ResponseBody
    public DataResult isOutOfNum(@RequestBody DlArrSetEntity bean){

        return DataResult.success(dlArrSetService.isOutOfNum(bean));
    }


    @ApiOperation(value = "更新")
    @PutMapping("dlArrSet/update")
    @RequiresPermissions("dlArrSet:update")
    @ResponseBody
    public DataResult update(@RequestBody DlArrSetEntity dlArrSet){
        dlArrSetService.updateById(dlArrSet);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlArrSet/listByPage")
    @RequiresPermissions("dlArrSet:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlArrSetEntity dlArrSet){
        Page page = new Page(dlArrSet.getPage(), dlArrSet.getLimit());
        LambdaQueryWrapper<DlArrSetEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlArrSetEntity::getId, dlArrSet.getId());
        IPage<DlArrSetEntity> iPage = dlArrSetService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlArrSet/list")
    @RequiresPermissions("dlArrSet:list")
    @ResponseBody
    public DataResult list(@RequestBody DlArrSetEntity dlArrSet){
        LambdaQueryWrapper<DlArrSetEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(dlArrSet.getType() != null)
            queryWrapper.eq(DlArrSetEntity::getType, dlArrSet.getType());
        if(!StringUtils.isEmpty(dlArrSet.getTypeId())){
            queryWrapper.eq(DlArrSetEntity::getTypeId, dlArrSet.getTypeId());
        }
        if(!StringUtils.isEmpty(dlArrSet.getArrId())){
            queryWrapper.eq(DlArrSetEntity::getArrId, dlArrSet.getArrId());
        }

        if(!StringUtils.isEmpty(dlArrSet.getTermId())){
            queryWrapper.eq(DlArrSetEntity::getTermId, dlArrSet.getTermId());
        }
        List<DlArrSetEntity> list = dlArrSetService.list(queryWrapper);
        return DataResult.success(list);
    }

    @ApiOperation(value = "查询预排课不能排的位置信息")
    @PostMapping("dlArrSet/findPreTableNoArrPosition")
    @RequiresPermissions("dlArrSet:list")
    @ResponseBody
    public DataResult findPreTableNoArrPosition(@RequestBody DlArrSetEntity dlArrSet){
        return dlArrSetService.findPreTableNoArrPosition(dlArrSet.getTypeId());
    }


}
