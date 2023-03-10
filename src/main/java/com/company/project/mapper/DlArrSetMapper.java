package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.DlArrSetEntity;

import java.util.List;

/**
 * 
 * 
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-19 15:22:38
 */
public interface DlArrSetMapper extends BaseMapper<DlArrSetEntity> {
    List<DlArrSetEntity> list(DlArrSetEntity bean);
}
