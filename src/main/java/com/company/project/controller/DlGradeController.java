package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlGradeEntity;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.service.DlGradeService;
import com.company.project.service.SysDictService;
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
public class DlGradeController {
    @Autowired
    private DlGradeService dlGradeService;

    @Autowired
    private SysDictService sysDictService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlGrade")
    public String dlGrade() {
        return "dlgrade/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlGrade/add")
    @RequiresPermissions("dlGrade:add")
    @ResponseBody
    public DataResult add(@RequestBody DlGradeEntity dlGrade){
        dlGrade = setGradeNameLabel(dlGrade);

        dlGradeService.save(dlGrade);
        dlGradeService.updateRedisGradeData();
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlGrade/delete")
    @RequiresPermissions("dlGrade:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){

        dlGradeService.removeByIds(ids);
        dlGradeService.updateRedisGradeData();
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlGrade/update")
    @RequiresPermissions("dlGrade:update")
    @ResponseBody
    public DataResult update(@RequestBody DlGradeEntity dlGrade){
        dlGrade = setGradeNameLabel(dlGrade);
        dlGradeService.updateById(dlGrade);
        dlGradeService.updateRedisGradeData();

        return DataResult.success();
    }


    public DlGradeEntity setGradeNameLabel(DlGradeEntity dlGrade){
        List<SysDictDetailEntity> list = sysDictService.getListByType("class_type", false);
        String type = "未定";
        for(SysDictDetailEntity bean : list){
            if(bean.getValue().equals(dlGrade.getType() + "")){
                type = bean.getLabel();
            }
        }
        dlGrade.setGradeNameLabel( type + "-" + dlGrade.getGradeName() + "级");

        return dlGrade;
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlGrade/listByPage")
    @RequiresPermissions("dlGrade:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlGradeEntity dlGrade){
        Page page = new Page(dlGrade.getPage(), dlGrade.getLimit());
        LambdaQueryWrapper<DlGradeEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlGradeEntity::getId, dlGrade.getId());
        if(!StringUtils.isEmpty(dlGrade.getType())){
            queryWrapper.eq(DlGradeEntity::getType, dlGrade.getType());
        }

        queryWrapper.orderByDesc(true, DlGradeEntity::getType);
        queryWrapper.orderByDesc(true, DlGradeEntity::getGradeName);

        IPage<DlGradeEntity> iPage = dlGradeService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "班级树形结构列表")
    @PostMapping("dlGrade/getClassTree")
    @RequiresPermissions("dlGrade:list")
    @ResponseBody
    public DataResult getClassTree(String classType){
        return DataResult.success(dlGradeService.getClassTree(classType));
    }

    @ApiOperation(value = "年级树形结构列表")
    @PostMapping("dlGrade/getGradeTree")
    @RequiresPermissions("dlGrade:list")
    @ResponseBody
    public DataResult getGradeTree(){
        return DataResult.success(dlGradeService.getGradeTree());
    }

    @ApiOperation(value = "年级树形结构列表")
    @PostMapping("dlGrade/getGradeTree2")
    @RequiresPermissions("dlGrade:list")
    @ResponseBody
    public DataResult getGradeTree2(){
        return DataResult.success(dlGradeService.getGradeTree2());
    }

    /**
     * 查询的是非毕业年级信息
     * @param dlGrade
     * @return
     */
    @ApiOperation(value = "通过教育阶段来查询年级信息")
    @PostMapping("dlGrade/getGradeListByClassType")
    @RequiresPermissions("dlGrade:list")
    @ResponseBody
    public DataResult getGradeListByClassType(@RequestBody DlGradeEntity dlGrade){
        LambdaQueryWrapper<DlGradeEntity> queryWrapper = Wrappers.lambdaQuery();
        if(!StringUtils.isEmpty(dlGrade.getType())){
            queryWrapper.eq(DlGradeEntity::getType, dlGrade.getType());
        }
        queryWrapper.eq(DlGradeEntity::getStatus, 1);
        return DataResult.success(dlGradeService.list(queryWrapper));
    }
}
