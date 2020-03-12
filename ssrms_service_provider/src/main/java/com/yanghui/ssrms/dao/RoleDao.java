package com.yanghui.ssrms.dao;

import com.github.pagehelper.Page;
import com.yanghui.ssrms.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色DAO
 */
public interface RoleDao {
    /**
     * 根据用户ID查询
     * @param userId
     * @return
     */
    List<Role> selectByUserId(@Param("userId") Integer userId);



    Page<Role> selectByCondition(@Param("queryString") String queryString);

    Role selectById(@Param("id") Integer id);

    List<Integer> selectMenuIdsByRoleId(@Param("id") Integer id);

    List<Integer> findPermissionIdsByRoleId(@Param("id") Integer id);

    void insert(Role role);

    void insertRoleAndMenu(@Param("id") Integer id, @Param("menuId") Integer menuId);

    void insertRoleAndPermission(@Param("id") Integer id, @Param("permissionId") Integer permissionId);

    void update(Role role);

    void deleteRoleAndMeunByRoleId(@Param("id") Integer id);

    void deleteRoleAndPremissionByRoleId(@Param("id") Integer id);

    List<Role> selectAll();

    Long countPermissionByRoleId(@Param("id") Integer id);

    Long countMenuByRoleId(@Param("id") Integer id);

    void deleteById(@Param("id") Integer id);
}
