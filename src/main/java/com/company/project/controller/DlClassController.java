package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlClassEntity;
import com.company.project.entity.DlGradeEntity;
import com.company.project.entity.DlStudentEntity;
import com.company.project.entity.SysUser;
import com.company.project.service.DlClassService;
import com.company.project.service.DlGradeService;
import com.company.project.service.DlStudentService;
import com.company.project.service.UserService;
import com.company.project.vo.resp.EChartDataVo;
import com.company.project.vo.resp.EChartVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlClassController {
    @Autowired
    private DlClassService dlClassService;

    @Autowired
    private DlGradeService dlGradeService;
    @Resource
    private DlStudentService dlStudentService;
    @Autowired
    private UserService userService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlClass")
    public String dlClass() {
        return "dlclass/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlClass/add")
    @RequiresPermissions("dlClass:add")
    @ResponseBody
    public DataResult add(@RequestBody DlClassEntity dlClass){
        DlGradeEntity dlGradeEntity = dlGradeService.getById(dlClass.getGradeId());
        if(dlGradeEntity != null){
            dlClass.setClassType(dlGradeEntity.getType());
            dlClass.setGradeNum(dlGradeEntity.getGradeNum());
        }
        dlClassService.save(dlClass);
        dlClassService.updateRedisClassData();
        return DataResult.success();
    }





    @ApiOperation(value = "删除")
    @DeleteMapping("dlClass/delete")
    @RequiresPermissions("dlClass:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        /*
        先检查这些班级下面是否还有一些班级下还有数据，不能够删除
         */
        List<String> new_ids = new ArrayList<>();
        boolean bool = false;
        LambdaQueryWrapper<DlStudentEntity> queryWrapper = null;
        for(String id : ids){
            queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(DlStudentEntity::getClassId, id);
            queryWrapper.eq(DlStudentEntity::getStatus, 1);//查看在校的学生
            int count = dlStudentService.count(queryWrapper);
            if(count > 0){
                bool = true;
            }else{
                new_ids.add(id);
            }
        }
        dlClassService.removeByIds(new_ids);
        dlClassService.updateRedisClassData();
        if(bool){
            if(new_ids.size() == 0){
                return DataResult.fail("班级下还有学生，不允许删除");
            }
            return DataResult.fail("部分删除了，不过部分班级下还有学生，不能进行删除");
        }
        return DataResult.success();
    }

    @ApiOperation(value = "获取详细的班级信息")
    @PostMapping("dlClass/detailsList")
    @RequiresPermissions("dlClass:list")
    @ResponseBody
    public DataResult getClassDetailsList(){
        return DataResult.success(dlClassService.getClassDetailsList());
    }


    @ApiOperation(value = "获取主页数据")
    @PostMapping("dlClass/getMainPageData")
    @RequiresPermissions("dlClass:list")
    @ResponseBody
    public DataResult getMainPageData(){
        //加载所有的主页面需要的数据
        Map<String, Object> map = new HashMap<>();
        //查询学生总数
        LambdaQueryWrapper<DlStudentEntity> sWrapper = Wrappers.lambdaQuery();
        sWrapper.eq(DlStudentEntity::getStatus, 1);
        Integer sCount = dlStudentService.count(sWrapper);
        map.put("sCount", sCount);
        //查询教师总数
        List<SysUser> users = userService.getTeacherList();
        if(users != null){
            map.put("tCount", users.size());
        }else{
            map.put("tCount", 0);
        }

        //查询所有员工数
        Integer num = userService.getUserCount();
        if(num != null){
            map.put("uCount", num);
        }else{
            map.put("uCount", 0);
        }

        //统计各个年级的人数
        List<DlClassEntity> list = dlClassService.getGradeStudentNumAndGradeName();
        EChartVo eChartVo = new EChartVo();
        List<Object> xAxis = new ArrayList<>();
        List<BigDecimal> series = new ArrayList<>();
        for (DlClassEntity bean: list){
            xAxis.add(bean.getGradeId());
            if(bean.getStuNum() == null){
                bean.setStuNum(0);
            }
            series.add(new BigDecimal(bean.getStuNum()));
        }
        eChartVo.setXAxis(xAxis);
        eChartVo.setXName("年级");
        EChartDataVo eChartDataVo = new EChartDataVo();
        eChartDataVo.setName("年级人数");
        eChartDataVo.setType("line");
        eChartDataVo.setData(series);
        List<Object> eChartDataVos = new ArrayList<>();
        eChartDataVos.add(eChartDataVo);
        eChartVo.setSeries(eChartDataVos);
        List<Object> legend = new ArrayList<>();
        legend.add("年级人数");
        eChartVo.setLegend(legend);
        System.out.println("----------------" + eChartVo);
        System.out.println("----------------" + sCount);
        map.put("eChartVo", eChartVo);


        return DataResult.success(map);
    }



    @ApiOperation(value = "更新")
    @PutMapping("dlClass/update")
    @RequiresPermissions("dlClass:update")
    @ResponseBody
    public DataResult update(@RequestBody DlClassEntity dlClass){
        DlGradeEntity dlGradeEntity = dlGradeService.getById(dlClass.getGradeId());
        if(dlGradeEntity != null){
            dlClass.setClassType(dlGradeEntity.getType());
            dlClass.setGradeNum(dlGradeEntity.getGradeNum());
        }
        dlClassService.updateById(dlClass);
        dlClassService.updateRedisClassData();
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlClass/listByPage")
    @RequiresPermissions("dlClass:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlClassEntity dlClass){
        Page page = new Page(dlClass.getPage(), dlClass.getLimit());
        LambdaQueryWrapper<DlClassEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(!StringUtils.isEmpty(dlClass.getGradeId()))
            queryWrapper.eq(DlClassEntity::getGradeId, dlClass.getGradeId());
        if(dlClass.getClassType() != null){
            queryWrapper.eq(DlClassEntity::getClassType, dlClass.getClassType());
        }
        IPage<DlClassEntity> iPage = dlClassService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlClass/getListByGradeId")
    @RequiresPermissions("dlClass:list")
    @ResponseBody
    public DataResult getListByGradeId(@RequestBody DlClassEntity dlClass){
        LambdaQueryWrapper<DlClassEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlClassEntity::getGradeId, dlClass.getGradeId());
        queryWrapper.eq(DlClassEntity::getStatus, 1);
        return DataResult.success(dlClassService.list(queryWrapper));
    }



}
