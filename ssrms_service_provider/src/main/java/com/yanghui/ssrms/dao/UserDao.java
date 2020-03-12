package com.yanghui.ssrms.dao;

import com.yanghui.ssrms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User fndByUsername(@Param("userName") String userName);

    void deleteByUsername(@Param("userName") String userName);

    void insertUser(User user);

    void updatePassword(@Param("username") String username, @Param("password") String password);

    List<Integer> findRoleIdsByUserId(@Param("id") Integer id);

    Integer selectUserIdByUsername(@Param("username") String username);
}
