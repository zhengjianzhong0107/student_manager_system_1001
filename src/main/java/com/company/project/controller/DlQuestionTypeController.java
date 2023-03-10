package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlQuestionTypeEntity;
import com.company.project.service.DlQuestionTypeService;
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
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-18 10:21:28
 */
@Controller
@RequestMapping("/")
public class DlQuestionTypeController {
    @Autowired
    private DlQuestionTypeService dlQuestionTypeService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlQuestionType")
    public String dlQuestionType() {
        return "dlquestiontype/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlQuestionType/add")
    @RequiresPermissions("dlQuestionType:add")
    @ResponseBody
    public DataResult add(@RequestBody DlQuestionTypeEntity dlQuestionType){
        dlQuestionTypeService.save(dlQuestionType);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlQuestionType/delete")
    @RequiresPermissions("dlQuestionType:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlQuestionTypeService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlQuestionType/update")
    @RequiresPermissions("dlQuestionType:update")
    @ResponseBody
    public DataResult update(@RequestBody DlQuestionTypeEntity dlQuestionType){
        dlQuestionTypeService.updateById(dlQuestionType);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlQuestionType/listByPage")
    @RequiresPermissions("dlQuestionType:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlQuestionTypeEntity dlQuestionType){
        Page page = new Page(dlQuestionType.getPage(), dlQuestionType.getLimit());
        LambdaQueryWrapper<DlQuestionTypeEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlQuestionTypeEntity::getId, dlQuestionType.getId());
        IPage<DlQuestionTypeEntity> iPage = dlQuestionTypeService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlQuestionType/getList")
    @RequiresPermissions("dlQuestionType:list")
    @ResponseBody
    public DataResult getList(){
        return DataResult.success(dlQuestionTypeService.list());
    }

}
