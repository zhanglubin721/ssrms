package com.yanghui.ssrms.dao;

import com.github.pagehelper.Page;
import com.yanghui.ssrms.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {
    void insert(Menu menu);

    Page<Menu> selectByConditon(@Param("queryString") String queryString);

    Long countRoleByMenuId(@Param("id") Integer id);

    void deleteById(@Param("id") Integer id);

    Menu seleceById(@Param("id") Integer id);

    void update(Menu menu);

    List<Menu> selectAll();


    Integer selectOnelevelById(@Param("menuId") Integer menuId);

    Menu selectOneLeverMenu(@Param("oneLeverMenuId") Integer oneLeverMenuId);

    Integer selectOnelevelIdByTwolevelId(@Param("menuId") Integer menuId);

//    Menu selectTwoLeverMenu(@Param("oneLeverMenuId") Integer oneLeverMenuIdLike);

}
