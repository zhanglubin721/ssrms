package com.yanghui.ssrms.dao;

import org.apache.ibatis.annotations.Param;

public interface UserRoleDao {
    void insertUserRole(@Param("user_id") int id, @Param("role_id") int role_id);

    void deleteByUserid(@Param("user_id") int user_id);
}
