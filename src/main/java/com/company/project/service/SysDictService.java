package com.company.project.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.entity.SysDictEntity;

import java.util.List;

/**
 * 数据字典 服务类
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public interface SysDictService extends IService<SysDictEntity> {
    List<SysDictDetailEntity> getListByType(String type, boolean orderBy);

    JSONArray getType(String name);
    List<SysDictDetailEntity> getTypeByNameAndValueLike(String name, String valueLike);
}

