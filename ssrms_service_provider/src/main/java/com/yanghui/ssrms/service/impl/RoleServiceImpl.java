package com.yanghui.ssrms.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yanghui.ssrms.dao.MenuDao;
import com.yanghui.ssrms.dao.RoleDao;
import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Role;
import com.yanghui.ssrms.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service(interfaceClass = RoleService.class)
@Slf4j
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        log.info("[角色-分页]data: {}",queryPageBean);
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> page = roleDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Role findById(Integer id) {
        log.info("[角色-根据id查询]data: {}",id);
        return roleDao.selectById(id);
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        log.info("[角色-根据检查组ID查询所有菜单项ID]data: {}",id);
        return roleDao.selectMenuIdsByRoleId(id);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        log.info("[角色-根据检查组ID查询所有权限ID]data: {}",id);
        return roleDao.findPermissionIdsByRoleId(id);
    }

    @Override
    public void add(Role role, Integer[] menuIds, Integer[] permissionIds) {
        log.info("[检查组-添加]data: {} checkitemIds: {} permissionIds: {}",role, menuIds,permissionIds);
        roleDao.insert(role);

        for (Integer menuId : menuIds) {
            roleDao.insertRoleAndMenu(role.getId(), menuId);
        }
        for (Integer permissionId : permissionIds) {
            roleDao.insertRoleAndPermission(role.getId(), permissionId);
        }
    }

    @Override
    public void edit(Role role, Integer[] menuIds, Integer[] permissionIds) {
        log.info("[角色-编辑角色]data: {} menuIds: {} permissionIds: {}",role, menuIds, permissionIds);
        roleDao.update(role);
        //更新检查组—检查项关系
        //删除原有关系
        roleDao.deleteRoleAndMeunByRoleId(role.getId());
        roleDao.deleteRoleAndPremissionByRoleId(role.getId());
        //添加新的关系
        for (Integer menuId : menuIds) {
            roleDao.insertRoleAndMenu(role.getId(), menuId);
        }
        for (Integer permissionId : permissionIds) {
            roleDao.insertRoleAndPermission(role.getId(), permissionId);
        }
    }

    @Override
    public List<Role> findAll() {
        log.info("[角色-查询所有]");
        List<Role> roles = roleDao.selectAll();
        return roles;
    }

    @Override
    public void delete(Integer id) {
        log.info("[角色-根据id删除]id：{}",id);
        //校验是否可以删除
        Long count1 = roleDao.countPermissionByRoleId(id);
        if(count1 >0){
            throw new RuntimeException("当前角色有数据，不能删除");
        }
        Long count2 = roleDao.countMenuByRoleId(id);
        if(count2 >0){
            throw new RuntimeException("当前角色有数据，不能删除");
        }
        //实际删除操作
        roleDao.deleteById(id);
    }

    @Override
    public Integer[] getTrueMenuIds(Integer[] menuIds) {
        /*
         * 处理menuIds得到我们想要的结果
         * */
        log.info("menuIdList>>>>>>>>>>  {}",menuIds);
        List<Integer> menuIdList =  Arrays.asList(menuIds);
        log.info("menuIdList>>>>>>>>>>  {}",menuIdList);




        List<Integer> onelevelIds = new ArrayList<>();
//        for (Integer menuId : menuIdList) {
//            Integer oneLevelMenuId = menuDao.selectOnelevelById(menuId);
//            if (null != oneLevelMenuId) {
//                onelevelIds.add(oneLevelMenuId);
//            }
//        }
        for (int i = 0; i < menuIdList.size(); i++) {
            Integer oneLevelMenuId = menuDao.selectOnelevelById(menuIdList.get(i));
            if (null != oneLevelMenuId) {
                onelevelIds.add(oneLevelMenuId);
            }
        }

        List<Integer> aaa = new ArrayList<>();

        for (int i = 0; i < menuIds.length; i++) {
            boolean x = false;
            Integer integer = menuDao.selectOnelevelById(menuIds[i]);
            if (null == integer) {
                Integer onelevelId = menuDao.selectOnelevelIdByTwolevelId(menuIds[i]);
                for (int j = 0; j < onelevelIds.size(); j++) {
                    if (onelevelIds.get(j) == onelevelId) {
                        x = true;
                    }
                }
                if (x == true) {
                    continue;
                }
                aaa.add(onelevelId);
            }
        }
        aaa.addAll(menuIdList);
        return aaa.toArray(new Integer[0]);
    }

}
