package com.yanghui.ssrms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yanghui.ssrms.dao.OrderDao;
import com.yanghui.ssrms.dao.SsrDao;
import com.yanghui.ssrms.entity.AdminQueryPageBean;
import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Order;
import com.yanghui.ssrms.pojo.Ssr;
import com.yanghui.ssrms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SsrDao ssrDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public PageResult StudentPageQuery(QueryPageBean queryPageBean, String username) {
        log.info("[自习室信息-分页查询]data：{}",queryPageBean);
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 分页查询数据库
        Page<Order> page = orderDao.selectByConditon(queryPageBean, username);
        //封装分页数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public PageResult adminPageQuery(AdminQueryPageBean adminQueryPageBean) {
        log.info("[自习室信息-分页查询]data：{}",adminQueryPageBean);
        //使用分页插件
        PageHelper.startPage(adminQueryPageBean.getCurrentPage(),adminQueryPageBean.getPageSize());
        // 分页查询数据库
        Page<Order> page = orderDao.adminSelectByConditon(adminQueryPageBean);
//        //封装分页数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void cancel(Long id) {
        log.info("[要删除的自习室id]ssrid:{}", id);
        orderDao.cancelById(id);
    }

    @Override
    public void add(Ssr ssr) {
        log.info("[添加自习室信息]ssr:{}", ssr);
        ssrDao.insertSsr(ssr);
    }

    @Override
    public Order findById(Long id) {
        log.info("[根据id查询自习室信息]id:{}", id);
        return orderDao.selectById(id);
    }

    @Override
    public void modify(Order order) {
        log.info("[编辑自习室信息]ssr:{}", order);
        orderDao.updateOrder(order);
    }

    @Override
    public void appointment(Long ssrid, String chooseDay, String chooseTime, String username) {
        log.info("[编辑自习室信息]ssrid:{}", ssrid);
        log.info("[编辑自习室信息]chooseDay:{}", chooseDay);
        log.info("[编辑自习室信息]chooseTime:{}", chooseTime);
        String stringSsrid = String.valueOf(ssrid);
        LocalDateTime now = LocalDateTime.now();
        Long orderId = getOrderId(ssrid, chooseDay, chooseTime);
        long longUsername = Long.parseLong(username);
        Order order = new Order(orderId, chooseDay, chooseTime, longUsername, stringSsrid, now, 1);
        orderDao.insertOrder(order);
    }


    public String getTomorrow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,1);
        return sdf.format(calendar.getTime());
    }

    public Long getOrderId(Long ssrid, String chooseDay, String chooseTime) {
        StringBuffer stringBuffer = new StringBuffer(chooseDay);
        int i = stringBuffer.indexOf("月");
        int i1 = stringBuffer.indexOf("号");
        String substring = stringBuffer.substring(0, i);
        String substring1 = stringBuffer.substring(i+1, i1);
        if (substring.length() == 1) {
            substring = "0" + substring;
        }
        String monthAndDay = substring + substring1;
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String stringSsrid = String.valueOf(ssrid);


        StringBuffer stringBuffer1 = new StringBuffer(chooseTime);
        int i2 = stringBuffer1.lastIndexOf("-");
        int i3 = stringBuffer1.lastIndexOf("点");
        String substring2 = stringBuffer1.substring(i2 + 1, i3);
        int i4 = stringBuffer1.indexOf("点", 1);
        String substring3 = stringBuffer1.substring(0, i4);
        if (substring3.length() == 1) {
            substring3 = "0" + substring3;
        }
        String time = substring3 + substring2;

        String stringOrderId = year + monthAndDay + stringSsrid + time;
        long orderId = Long.parseLong(stringOrderId);
        return orderId;
    }

}
