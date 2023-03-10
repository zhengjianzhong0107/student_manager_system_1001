package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DlGradeEntity;
import com.company.project.vo.resp.MenuTreeVo;

import java.util.List;

/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
public interface DlGradeService extends IService<DlGradeEntity> {
    void updateRedisGradeData();
    List<DlGradeEntity> getGradeList();
    List<MenuTreeVo> getClassTree(String classType);

    List<MenuTreeVo> getGradeTree();

    List<MenuTreeVo> getGradeTree2();
}

