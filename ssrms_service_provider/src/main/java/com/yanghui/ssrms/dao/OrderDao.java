package com.yanghui.ssrms.dao;

import com.github.pagehelper.Page;
import com.yanghui.ssrms.entity.AdminQueryPageBean;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDao {
    void insertOrder(Order order);

    Page<Order> selectByConditon(@Param("queryPageBean") QueryPageBean queryPageBean, @Param("username") String username);

    Page<Order> adminSelectByConditon(AdminQueryPageBean adminQueryPageBean);

    void cancelById(@Param("id") Long id);

    void updateOrder(Order order);

    Order selectById(Long id);
}
