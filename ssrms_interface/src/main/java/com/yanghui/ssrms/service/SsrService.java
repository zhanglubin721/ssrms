package com.yanghui.ssrms.service;


import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.entity.SsrPageResult;
import com.yanghui.ssrms.pojo.Ssr;

public interface SsrService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    SsrPageResult pageQuery(QueryPageBean queryPageBean);

    void delete(Long ssrid);

    void add(Ssr ssr);

    Ssr findBySid(Long ssrid);

    void edit(Ssr ssr);

    void appointment(Long ssrid, String chooseDay, String chooseTime, String username);

    void testRabbitmq(String message);
}
