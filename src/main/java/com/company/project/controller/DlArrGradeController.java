package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.enums.CheckBoxStatusEnum;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrGradeEntity;
import com.company.project.entity.DlGradeEntity;
import com.company.project.service.DlArrGradeService;
import com.company.project.service.DlGradeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-02-21 12:50:04
 */
@Controller
@RequestMapping("/")
public class DlArrGradeController {
    @Autowired
    private DlArrGradeService dlArrGradeService;

    @Autowired
    private DlGradeService dlGradeService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlArrGrade")
    public String dlArrGrade() {
        return "dlarrgrade/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlArrGrade/add")
    @RequiresPermissions("dlArrGrade:add")
    @ResponseBody
    public DataResult add(@RequestBody DlArrGradeEntity dlArrGrade){
        dlArrGradeService.save(dlArrGrade);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlArrGrade/delete")
    @RequiresPermissions("dlArrGrade:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlArrGradeService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlArrGrade/update")
    @RequiresPermissions("dlArrGrade:update")
    @ResponseBody
    public DataResult update(@RequestBody DlArrGradeEntity dlArrGrade){
        dlArrGradeService.updateById(dlArrGrade);
        return DataResult.success();
    }



    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlArrGrade/listByPage")
    @RequiresPermissions("dlArrGrade:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlArrGradeEntity dlArrGrade){
        Page page = new Page(dlArrGrade.getPage(), dlArrGrade.getLimit());
        LambdaQueryWrapper<DlArrGradeEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlArrGradeEntity::getId, dlArrGrade.getId());
        IPage<DlArrGradeEntity> iPage = dlArrGradeService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询可以排课年级列表信息")
    @PostMapping("dlArrGrade/list")
    @RequiresPermissions("dlArrGrade:list")
    @ResponseBody
    public DataResult list(@RequestBody DlArrGradeEntity dlArrGrade){
        LambdaQueryWrapper<DlArrGradeEntity> wrapper = Wrappers.lambdaQuery();

        //查询所有年级信息
        List<DlGradeEntity> allList = dlGradeService.getGradeList();


        //查询所有已排年级信息
        wrapper.eq(DlArrGradeEntity::getTermId, dlArrGrade.getTermId());
        wrapper.ne(DlArrGradeEntity::getArrId, dlArrGrade.getArrId());

        List<DlArrGradeEntity> arrList = dlArrGradeService.list(wrapper);
        Set<String> set = new HashSet<>();
        for (DlArrGradeEntity bean: arrList){
            bean.setStatus(CheckBoxStatusEnum.DISABLE.value);
            set.add(bean.getGradeId());
        }

        //查询本次任务已经排的所有信息
        if(!StringUtils.isEmpty(dlArrGrade.getArrId())){
            wrapper.clear();
            wrapper.eq(DlArrGradeEntity::getArrId, dlArrGrade.getArrId());
            wrapper.eq(DlArrGradeEntity::getTermId, dlArrGrade.getTermId());
            List<DlArrGradeEntity> thisList = dlArrGradeService.list(wrapper);
            if(thisList != null)
                for (DlArrGradeEntity bean: thisList){
                    bean.setStatus(CheckBoxStatusEnum.CHECKED.value);
                    arrList.add(bean);
                    set.add(bean.getGradeId());
                }
        }
        DlArrGradeEntity entity = null;
        for (DlGradeEntity bean: allList){
            if(!set.contains(bean.getId())){
                entity = new DlArrGradeEntity();
                entity.setGradeId(bean.getId());
                entity.setGradeName(bean.getGradeNameLabel());
                entity.setStatus(CheckBoxStatusEnum.UNCHECKED.value);
                arrList.add(entity);
            }
        }
        return DataResult.success(arrList);
    }

}
