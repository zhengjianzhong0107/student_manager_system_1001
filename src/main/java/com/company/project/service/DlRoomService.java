package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlRoomEntity;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlRoomService extends IService<DlRoomEntity> {
    DataResult updateByPrimaryKeySelective(DlRoomEntity bean);

    void updateRedisRoomData();
}

