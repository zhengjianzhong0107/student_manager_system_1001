package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlCourseTableEntity;
import com.company.project.service.DlCourseTableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
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
 * @date 2021-02-07 10:45:06
 */
@Controller
@RequestMapping("/")
public class DlCourseTableController {
    @Autowired
    private DlCourseTableService dlCourseTableService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlCourseTable")
    public String dlCourseTable() {
        return "dlcoursetable/list";
        }

    /**
     * 跳转到页面
     */
    @GetMapping("/index/teacherTable")
    public String teacherCourseTab() {
        return "dlcoursetable/teacherTable";
    }

    /**
     * 跳转到页面
     */
    @GetMapping("/index/classTable")
    public String classCourseTab() {
        return "dlcoursetable/classTable";
    }

    @GetMapping("/index/exchangeCoursePage")
    public String exchangeCoursePage(){
        return "dlcoursetable/exchange";
    }

    @ApiOperation(value = "新增")
    @PostMapping("dlCourseTable/add")
    @RequiresPermissions("dlCourseTable:add")
    @ResponseBody
    public DataResult add(@RequestBody DlCourseTableEntity dlCourseTable){
        dlCourseTableService.save(dlCourseTable);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlCourseTable/delete")
    @RequiresPermissions("dlCourseTable:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlCourseTableService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlCourseTable/update")
    @RequiresPermissions("dlCourseTable:update")
    @ResponseBody
    public DataResult update(@RequestBody DlCourseTableEntity dlCourseTable){
        dlCourseTableService.updateById(dlCourseTable);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlCourseTable/listByPage")
    @RequiresPermissions("dlCourseTable:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlCourseTableEntity dlCourseTable){
        Page page = new Page(dlCourseTable.getPage(), dlCourseTable.getLimit());
        LambdaQueryWrapper<DlCourseTableEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.eq(DlCourseTableEntity::getArrId, dlCourseTable.getArrId());
//        if(StringUtils.isNotEmpty(dlCourseTable.getCourseName())){
//            queryWrapper.and(i -> i.like(DlCourseTableEntity::getCourseName, dlCourseTable.getCourseName())
//                    .or().like(DlCourseTableEntity::getClassName, dlCourseTable.getCourseName())
//                    .or().like(DlCourseTableEntity::getTeacherName, dlCourseTable.getCourseName()));
//        }

        IPage<DlCourseTableEntity> iPage = dlCourseTableService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


    @ApiOperation(value = "查询课表数据")
    @PostMapping("dlCourseTable/tableData")
    @RequiresPermissions("dlCourseTable:list")
    @ResponseBody
    public DataResult getData(@RequestBody DlCourseTableEntity dlCourseTable){
        LambdaQueryWrapper<DlCourseTableEntity> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotEmpty(dlCourseTable.getTermId()) ){
            queryWrapper.eq(DlCourseTableEntity::getTermId, dlCourseTable.getTermId());
        }else{
            return DataResult.fail("请选择学期");
        }
        if(StringUtils.isNotEmpty(dlCourseTable.getClassId())){
            queryWrapper.eq(DlCourseTableEntity::getClassId, dlCourseTable.getClassId());
        }
        if(StringUtils.isNotEmpty(dlCourseTable.getArrId())){
            queryWrapper.eq(DlCourseTableEntity::getArrId, dlCourseTable.getArrId());
        }

        if(StringUtils.isNotEmpty(dlCourseTable.getTeacherId())){
            queryWrapper.eq(DlCourseTableEntity::getTeacherId, dlCourseTable.getTeacherId());
        }
        return DataResult.success(dlCourseTableService.list(queryWrapper));
    }



    @ApiOperation(value = "查询课表数据")
    @PostMapping("dlCourseTable/table")
    @RequiresPermissions("dlCourseTable:list")
    @ResponseBody
    public DataResult getCourseTable(@RequestBody DlCourseTableEntity dlCourseTable){
        System.out.println(dlCourseTable);
        LambdaQueryWrapper<DlCourseTableEntity> queryWrapper = Wrappers.lambdaQuery();

        if(StringUtils.isNotEmpty(dlCourseTable.getClassId())){
            queryWrapper.eq(DlCourseTableEntity::getClassId, dlCourseTable.getClassId());
        }else {
            return DataResult.fail("请选择班级");
        }
        if(StringUtils.isNotEmpty(dlCourseTable.getTermId()) ){
            queryWrapper.eq(DlCourseTableEntity::getTermId, dlCourseTable.getTermId());
        }
        if(StringUtils.isNotEmpty(dlCourseTable.getArrId())){
            queryWrapper.eq(DlCourseTableEntity::getArrId, dlCourseTable.getArrId());
        }
        return DataResult.success(dlCourseTableService.list(queryWrapper));
    }

    @ApiOperation(value = "查询个人课表数据")
    @PostMapping("dlCourseTable/myTable")
    @RequiresPermissions("dlCourseTable:list")
    @ResponseBody
    public DataResult getMyCourseTable(@RequestBody DlCourseTableEntity dlCourseTable){


        return DataResult.success();
    }


    @ApiOperation(value = "调整课表")
    @PostMapping("dlCourseTable/exchange")
    @RequiresPermissions("dlCourseTable:exchange")
    @ResponseBody
    public DataResult exchangeCourse(@RequestBody DlCourseTableEntity dlCourseTable){//id表示当前需要调课的ID，col和row是需要调整到的位置坐标
        DlCourseTableEntity oldBean = dlCourseTableService.getById(dlCourseTable.getId());
        LambdaQueryWrapper<DlCourseTableEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlCourseTableEntity::getClassId, oldBean.getClassId());
        queryWrapper.eq(DlCourseTableEntity::getCol, dlCourseTable.getCol());
        queryWrapper.eq(DlCourseTableEntity::getArrId, oldBean.getArrId());
        queryWrapper.eq(DlCourseTableEntity::getRow, dlCourseTable.getRow());
        List<DlCourseTableEntity> list = dlCourseTableService.list(queryWrapper);
        if(list != null && list.size() != 0){
            DlCourseTableEntity tempBean = list.get(0);
            tempBean.setRow(oldBean.getRow());
            tempBean.setCol(oldBean.getCol());
            dlCourseTableService.updateById(tempBean);
        }
        oldBean.setCol(dlCourseTable.getCol());
        oldBean.setRow(dlCourseTable.getRow());
        dlCourseTableService.updateById(oldBean);
        return DataResult.success();
    }

    @ApiOperation(value = "获取可以排课列表")
    @PostMapping("dlCourseTable/getAllowPosition")
    @RequiresPermissions("dlCourseTable:exchange")
    @ResponseBody
    public DataResult getAllowPosition(@RequestBody DlCourseTableEntity dlCourseTable){
        if(StringUtils.isEmpty(dlCourseTable.getId())){
            return DataResult.fail("主键缺失");
        }
        return dlCourseTableService.findAllowPosition(dlCourseTable.getId());
    }

    @ApiOperation(value = "获取教师课程列表")
    @PostMapping("dlCourseTable/getListById")
    @RequiresPermissions("dlCourseTable:exchange")
    @ResponseBody
    public DataResult getListByTeacherId(@RequestBody DlCourseTableEntity dlCourseTable){
        if(StringUtils.isEmpty(dlCourseTable.getId())){
            return DataResult.success();
        }
        DlCourseTableEntity bean = dlCourseTableService.getById(dlCourseTable.getId());
        LambdaQueryWrapper<DlCourseTableEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlCourseTableEntity::getArrId, bean.getArrId());
        queryWrapper.eq(DlCourseTableEntity::getTeacherId, bean.getTeacherId());
        return DataResult.success(dlCourseTableService.list(queryWrapper));
    }




}
