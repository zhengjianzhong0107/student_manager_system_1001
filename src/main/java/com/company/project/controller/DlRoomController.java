package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import com.company.project.common.utils.DataResult;

import com.company.project.entity.DlRoomEntity;
import com.company.project.service.DlRoomService;



/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlRoomController {
    @Autowired
    private DlRoomService dlRoomService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlRoom")
    public String dlRoom() {
        return "dlroom/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlRoom/add")
    @RequiresPermissions("dlRoom:add")
    @ResponseBody
    public DataResult add(@RequestBody DlRoomEntity dlRoom){
        dlRoomService.save(dlRoom);
        dlRoomService.updateRedisRoomData();
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlRoom/delete")
    @RequiresPermissions("dlRoom:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlRoomService.removeByIds(ids);
        dlRoomService.updateRedisRoomData();
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlRoom/update")
    @RequiresPermissions("dlRoom:update")
    @ResponseBody
    public DataResult update(@RequestBody DlRoomEntity dlRoom){
        dlRoomService.updateById(dlRoom);
        dlRoomService.updateRedisRoomData();
        return DataResult.success();
    }

    @ApiOperation(value = "更新状态")
    @PutMapping("dlRoom/updateStatus")
    @RequiresPermissions("dlRoom:updateStatus")
    @ResponseBody
    public DataResult updateStatus(@RequestBody DlRoomEntity dlRoom){
        DataResult result = dlRoomService.updateByPrimaryKeySelective(dlRoom);
        dlRoomService.updateRedisRoomData();
        return result;
    }



    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlRoom/listByPage")
    @RequiresPermissions("dlRoom:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlRoomEntity dlRoom){
        Page page = new Page(dlRoom.getPage(), dlRoom.getLimit());
        LambdaQueryWrapper<DlRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlRoomEntity::getId, dlRoom.getId());
        if(!StringUtils.isEmpty(dlRoom.getRoomName()))
            queryWrapper.like(DlRoomEntity::getRoomName, dlRoom.getRoomName());
        if(!StringUtils.isEmpty(dlRoom.getRoomType()))
            queryWrapper.eq(DlRoomEntity::getRoomType, dlRoom.getRoomType());
        IPage<DlRoomEntity> iPage = dlRoomService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
