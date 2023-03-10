package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrSetEntity;

/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-19 15:22:38
 */
public interface DlArrSetService extends IService<DlArrSetEntity> {
    boolean isOutOfNum(DlArrSetEntity bean);
    DataResult findPreTableNoArrPosition(String typeId);
}

