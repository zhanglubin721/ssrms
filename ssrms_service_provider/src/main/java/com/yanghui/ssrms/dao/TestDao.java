package com.yanghui.ssrms.dao;


import com.yanghui.ssrms.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface TestDao {
    User testLogin(@Param("userName") String name);
}
