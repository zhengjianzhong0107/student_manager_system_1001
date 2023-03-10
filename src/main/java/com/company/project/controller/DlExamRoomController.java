package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamRoomEntity;
import com.company.project.service.DlExamRoomService;
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
 * @date 2020-12-04 18:03:46
 */
@Controller
@RequestMapping("/")
public class DlExamRoomController {
    @Autowired
    private DlExamRoomService dlExamRoomService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamRoom")
    public String dlExamRoom() {
        return "dlexamroom/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamRoom/add")
    @RequiresPermissions("dlExamRoom:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamRoomEntity dlExamRoom){
        if(StringUtils.isEmpty(dlExamRoom.getExamId())){
            return DataResult.fail("考试计划主键缺失，请退出重新再试");
        }
        LambdaQueryWrapper<DlExamRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamRoomEntity::getExamId, dlExamRoom.getExamId());
        queryWrapper.eq(DlExamRoomEntity::getRoomId, dlExamRoom.getRoomId());
        int count = dlExamRoomService.count(queryWrapper);
        if(count > 0){
            return DataResult.fail("已经添加该教室了，请重新选择");
        }

        dlExamRoomService.save(dlExamRoom);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamRoom/delete")
    @RequiresPermissions("dlExamRoom:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamRoomService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamRoom/update")
    @RequiresPermissions("dlExamRoom:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamRoomEntity dlExamRoom){
        LambdaQueryWrapper<DlExamRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamRoomEntity::getExamId, dlExamRoom.getExamId());
        queryWrapper.eq(DlExamRoomEntity::getRoomName, dlExamRoom.getRoomName());
        int count = dlExamRoomService.count(queryWrapper);
        if(count > 0){
            return DataResult.fail("考场名称重复，请重新填写");
        }
        dlExamRoomService.updateById(dlExamRoom);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamRoom/listByPage")
    @RequiresPermissions("dlExamRoom:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamRoomEntity dlExamRoom){
        Page page = new Page(dlExamRoom.getPage(), dlExamRoom.getLimit());
        LambdaQueryWrapper<DlExamRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.eq(DlExamRoomEntity::getExamId, dlExamRoom.getExamId());
        IPage<DlExamRoomEntity> iPage = dlExamRoomService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "检查考场容量是否满足条件")
    @PostMapping("dlExamRoom/roomIsEnough")
    @RequiresPermissions("dlExamRoom:list")
    @ResponseBody
    public DataResult roomIsEnough(@RequestBody DlExamRoomEntity dlExamRoom){
        if(StringUtils.isEmpty(dlExamRoom.getExamId())){
            return DataResult.fail("考试计划ID未传入");
        }
        return DataResult.success(dlExamRoomService.checkRoomIsEnough(dlExamRoom.getExamId()));
    }

}
