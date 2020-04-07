package com.yanghui.ssrms.service;


import com.yanghui.ssrms.entity.AdminQueryPageBean;
import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.entity.SsrPageResult;
import com.yanghui.ssrms.pojo.Order;
import com.yanghui.ssrms.pojo.Ssr;

public interface OrderService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult StudentPageQuery(QueryPageBean queryPageBean, String username);

    PageResult adminPageQuery(AdminQueryPageBean adminQueryPageBean);

    void cancel(Long id);

    void add(Ssr ssr);

    Order findById(Long id);

    void modify(Order order);

    void appointment(Long ssrid, String chooseDay, String chooseTime, String username);

}
