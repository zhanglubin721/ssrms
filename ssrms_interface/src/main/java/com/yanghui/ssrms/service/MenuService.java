package com.yanghui.ssrms.service;




import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Menu;

import java.util.List;

public interface MenuService {
    void add(Menu menu);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void delete(Integer id);

    Menu findById(Integer id);

    void edit(Menu menu);

    List<Menu> findAll();

    List<Integer> findOnelevelById(List<Integer> menuIdsAll);

    List<Menu> findmenuInformation(List<Integer> oneLeverMenuIds, List<Integer> menuIdsAll);
}
