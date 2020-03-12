package com.yanghui.ssrms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yanghui.ssrms.dao.MenuDao;
import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Menu;
import com.yanghui.ssrms.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MenuService.class)
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public void add(Menu menu) {
        menuDao.insert(menu);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        log.info("[菜单项-分页查询]data：{}", queryPageBean);
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Menu> page = menuDao.selectByConditon(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        log.info("[菜单项-根据id删除]id：{}", id);
        //校验是否可以删除
        Long count = menuDao.countRoleByMenuId(id);
        if (count > 0) {
            throw new RuntimeException("当前菜单项有数据，不能删除");
        }
        //实际删除操作
        menuDao.deleteById(id);
    }

    @Override
    public Menu findById(Integer id) {
        log.info("[菜单项-根据id查询]id：{}", id);
        return menuDao.seleceById(id);
    }

    @Override
    public void edit(Menu menu) {
        log.info("[菜单项-编辑]data：{}", menu);
        menuDao.update(menu);
    }

    @Override
    public List<Menu> findAll() {
        log.info("[菜单项-查询所有]");
        List<Menu> menus = menuDao.selectAll();
        return menus;
    }

    @Override
    public List<Integer> findOnelevelById(List<Integer> menuIdsAll) {
        log.info("[菜单项-查询所有一级菜单]");
        List<Integer> oneLeverMenuIds = new ArrayList<>();
        for (Integer menuId : menuIdsAll) {
            Integer oneLevelMenuId = menuDao.selectOnelevelById(menuId);
            if (null != oneLevelMenuId) {
                oneLeverMenuIds.add(oneLevelMenuId);
            }
        }
        return oneLeverMenuIds;


    }

    @Override
    public List<Menu> findmenuInformation(List<Integer> oneLeverMenuIds, List<Integer> menuIdsAll) {
        List<Menu> menuList = new ArrayList<>();

        for (Integer oneLeverMenuId : oneLeverMenuIds) {
            menuList.add(menuDao.selectOneLeverMenu(oneLeverMenuId));
        }
        for (Menu menu : menuList) {
            List<Menu> children = menu.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                boolean x = false;
                /* 判断这个childMenu是否在menuIdsAll集合中,如果不在证明不应该有这个二级菜单
                 *  那就把这个二级菜单从它所属的一级菜单的children集合中删除,最后把处理完的menuList返回
                 * */
                for (int z = 0; z < menuIdsAll.size(); z++) {
                    if (children.get(i).getId() == menuIdsAll.get(z)) {
                        x = true;
                        break;
                    }
                }
                if (true == x) {
                    continue;
                }
                children.remove(children.get(i));
            }
        }

        return menuList;
    }
}
