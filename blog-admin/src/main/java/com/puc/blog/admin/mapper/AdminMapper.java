package com.puc.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puc.blog.admin.pojo.Admin;
import com.puc.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    @Select("SELECT * from ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{id})")
    List<Permission> findPermissionByAdminId(Long id);
}
