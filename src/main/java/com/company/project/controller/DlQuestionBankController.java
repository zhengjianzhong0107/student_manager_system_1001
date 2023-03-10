package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlQuestionBankEntity;
import com.company.project.service.DlQuestionBankService;
import com.company.project.service.HttpSessionService;
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
public class DlQuestionBankController {
    @Autowired
    private DlQuestionBankService dlQuestionBankService;

    @Autowired
    private HttpSessionService httpSessionService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlQuestionBank")
    public String dlQuestionBank() {
        return "dlquestionbank/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlQuestionBank/add")
    @RequiresPermissions("dlQuestionBank:add")
    @ResponseBody
    public DataResult add(@RequestBody DlQuestionBankEntity dlQuestionBank){
        dlQuestionBankService.save(dlQuestionBank);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlQuestionBank/delete")
    @RequiresPermissions("dlQuestionBank:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlQuestionBankService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlQuestionBank/update")
    @RequiresPermissions("dlQuestionBank:update")
    @ResponseBody
    public DataResult update(@RequestBody DlQuestionBankEntity dlQuestionBank){
        dlQuestionBankService.updateById(dlQuestionBank);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlQuestionBank/listByPage")
    @RequiresPermissions("dlQuestionBank:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlQuestionBankEntity dlQuestionBank){
        System.out.println("**************************");
        System.out.println(dlQuestionBank);
        Page page = new Page(dlQuestionBank.getPage(), dlQuestionBank.getLimit());
        LambdaQueryWrapper<DlQuestionBankEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlQuestionBank.getGradeType()))
            queryWrapper.eq(DlQuestionBankEntity::getGradeType, dlQuestionBank.getGradeType());
        if(!StringUtils.isEmpty(dlQuestionBank.getContent()))
            queryWrapper.like(DlQuestionBankEntity::getContent, dlQuestionBank.getContent());
        if(!StringUtils.isEmpty(dlQuestionBank.getQuestionType()))
            queryWrapper.eq(DlQuestionBankEntity::getQuestionType, dlQuestionBank.getQuestionType());
        if(!StringUtils.isEmpty(dlQuestionBank.getSubjectId()))
            queryWrapper.eq(DlQuestionBankEntity::getSubjectId, dlQuestionBank.getSubjectId());
        //只能查看共有的和自己的
        queryWrapper.and(Wrapper->Wrapper.eq(DlQuestionBankEntity::getSelf, 2).or().eq(DlQuestionBankEntity::getCreateId, httpSessionService.getCurrentUserId()));
        
        IPage<DlQuestionBankEntity> iPage = dlQuestionBankService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
