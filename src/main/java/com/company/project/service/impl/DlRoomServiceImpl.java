package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.Constant;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.service.RedisService;
import com.company.project.service.SysDictService;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.DlRoomMapper;
import com.company.project.entity.DlRoomEntity;
import com.company.project.service.DlRoomService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("dlRoomService")
public class DlRoomServiceImpl extends ServiceImpl<DlRoomMapper, DlRoomEntity> implements DlRoomService {
    @Resource
    private DlRoomMapper mapper;

    @Resource
    private RedisService redisService;

    @Resource
    private SysDictService sysDictService;

    @Transactional
    public DataResult updateByPrimaryKeySelective(DlRoomEntity bean){
        baseMapper.updateById(bean);
        return DataResult.success();
    }

    /**
     * 获取所有的教室信息
     * @return
     */
    public JSONArray getRooms(){
        LambdaQueryWrapper<DlRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(true, DlRoomEntity::getRoomType);
        List<DlRoomEntity> list = mapper.selectList(queryWrapper);
        List<SysDictDetailEntity> classroom_type = sysDictService.getListByType("classroom_type", false);
        for(DlRoomEntity d: list){
            for(SysDictDetailEntity s: classroom_type){
                if(d.getRoomType().toString().equals(s.getValue())){
                    d.setClassRoomType(s);
                }
            }
        }
        return JSONArray.parseArray(JSON.toJSONString(list));
    }

    @Override
    public void updateRedisRoomData(){
        LambdaQueryWrapper<DlRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(true, DlRoomEntity::getRoomType);
        List<DlRoomEntity> list = mapper.selectList(queryWrapper);
        redisService.set(Constant.CLASSROOM_DATA_KEY, JSON.toJSONString(list));
    }
}