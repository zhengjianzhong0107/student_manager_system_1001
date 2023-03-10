package com.company.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.SysFilesEntity;
import com.company.project.service.SysFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


/**
 * 文件上传
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/sysFiles")
@Api(tags = "文件管理")
public class SysFilesController {
    @Resource
    private SysFilesService sysFilesService;

    @ApiOperation(value = "新增")
    @PostMapping("/upload")
    @RequiresPermissions(value = {"sysFiles:add", "sysContent:update", "sysContent:add"}, logical = Logical.OR)
    public DataResult add(@RequestParam(value = "file") MultipartFile file) {
        //判断文件是否空
        if (file == null || file.getOriginalFilename() == null || "".equalsIgnoreCase(file.getOriginalFilename().trim())) {
            return DataResult.fail("文件为空");
        }
        return sysFilesService.saveFile(file);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    @RequiresPermissions("sysFiles:delete")
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        sysFilesService.removeByIdsAndFiles(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("/listByPage")
    @RequiresPermissions("sysFiles:list")
    public DataResult findListByPage(@RequestBody SysFilesEntity sysFiles) {
        Page page = new Page(sysFiles.getPage(), sysFiles.getLimit());
        IPage<SysFilesEntity> iPage = sysFilesService.page(page, Wrappers.<SysFilesEntity>lambdaQuery().orderByDesc(SysFilesEntity::getCreateDate));
        return DataResult.success(iPage);
    }


    @GetMapping("/download")
    @RequiresPermissions("sysFiles:download")
    public void downloadLocal(HttpServletResponse response, @RequestParam String url) throws Exception {
        System.out.println("url:" + url);
        /** 获取静态文件的路径 .*/
        String path = ResourceUtils.getURL("classpath:").getPath() + "static" + url;
        System.out.println("path:" + path);
        /** 获取文件的名称 . */
        String fileName = path.substring(path.lastIndexOf("/") +1);
        File file = new File(path);
        if (!file.exists()){
            System.out.println("路径有误，文件不存在！");
            return ;
        }
        System.out.println("fileName:" + fileName);
        /** 将文件名称进行编码 */
        response.setCharacterEncoding("utf-8");
        response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
        response.setContentType("application/octet-stream;charset=UTF-8");
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1){ /** 将流中内容写出去 .*/
            outputStream.write(buffer ,0 , len);
        }


        inputStream.close();
        outputStream.close();
    }


}
