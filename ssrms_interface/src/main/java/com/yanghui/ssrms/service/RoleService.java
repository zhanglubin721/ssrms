package com.yanghui.ssrms.service;




import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Role;

import java.util.List;

public interface RoleService {
    PageResult findPage(QueryPageBean queryPageBean);

    Role findById(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    void add(Role role, Integer[] menuIds, Integer[] permissionIds);

    void edit(Role role, Integer[] menuIds, Integer[] permissionIds);

    List<Role> findAll();

    void delete(Integer id);

    Integer[] getTrueMenuIds(Integer[] menuIds);
}
