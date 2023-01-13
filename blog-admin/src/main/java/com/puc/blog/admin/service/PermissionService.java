package com.puc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puc.blog.admin.mapper.PermissionMapper;
import com.puc.blog.admin.model.params.PageParams;
import com.puc.blog.admin.pojo.Permission;
import com.puc.blog.admin.vo.PageResult;
import com.puc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    public Result listPermission(PageParams pageParams) {
        /**
         * 要的数据 表的所有字段 Permission
         *
         */
        Page<Permission> page = new Page<>(pageParams.getCurrentPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(pageParams.getQueryString())){
            queryWrapper.eq(Permission::getName, pageParams.getQueryString());
        }
        Page<Permission> permissionPage = permissionMapper.selectPage(page, queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return Result.success(pageResult);
    }

    public Result add(Permission permission) {
        this.permissionMapper.insert(permission);
        return Result.success(null);
    }

    public Result update(Permission permission) {
        this.permissionMapper.updateById(permission);
        return Result.success(null);
    }

    public Result delete(Long id) {
        this.permissionMapper.deleteById(id);
        return Result.success(null);
    }
}
