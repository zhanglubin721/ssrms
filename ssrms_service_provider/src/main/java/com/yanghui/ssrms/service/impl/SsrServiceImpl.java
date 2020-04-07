package com.yanghui.ssrms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yanghui.ssrms.dao.OrderDao;
import com.yanghui.ssrms.dao.SsrDao;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.entity.SsrPageResult;
import com.yanghui.ssrms.pojo.Order;
import com.yanghui.ssrms.pojo.Ssr;
import com.yanghui.ssrms.service.SsrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SsrServiceImpl implements SsrService {

    @Autowired
    private SsrDao ssrDao;


    @Autowired
    private OrderDao orderDao;


    @Override
    public SsrPageResult pageQuery(QueryPageBean queryPageBean) {
        log.info("[自习室信息-分页查询]data：{}",queryPageBean);
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 分页查询数据库
        Page<Ssr> page = ssrDao.selectByConditon(queryPageBean);
        log.info("[测试获取查询日期]day:{}", queryPageBean.getChooseDay());
        log.info("[测试获取查询时间段]time:{}", queryPageBean.getChooseTime());

        //处理可选日期和可选时间段
        LocalDateTime dateTime = LocalDateTime.now();
        String month = String.valueOf(dateTime.getMonthValue());
        String today = String.valueOf(dateTime.getDayOfMonth());
        int hour = dateTime.getHour();

        //得到明天的日期
        String tomorrow = getTomorrow();


        ArrayList<HashMap> optionalDay = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("labelDay", month+"月"+today+"号");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("labelDay", month+"月"+tomorrow+"号");
        optionalDay.add(map);
        optionalDay.add(map2);


        ArrayList<HashMap> optionalTime = new ArrayList<>();

        //初始化下拉框数据
        HashMap<String, String> map5 = new HashMap<>();
        map5.put("labelTime", "18点-21点");
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("labelTime", "15点-18点");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("labelTime", "12点-15点");
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("labelTime", "9点-12点");

        if (hour >= 15 && hour < 18) {
            optionalTime.add(map5);
        } else if (hour >= 12 && hour < 15) {
            optionalTime.add(map4);
            optionalTime.add(map5);
        } else if (hour >= 9 && hour < 12) {
            optionalTime.add(map3);
            optionalTime.add(map4);
            optionalTime.add(map5);
        } else {
            optionalTime.add(map1);
            optionalTime.add(map3);
            optionalTime.add(map4);
            optionalTime.add(map5);
        }

        //封装分页数据
        return new SsrPageResult(page.getTotal(),page.getResult(), optionalDay, optionalTime);
    }

    @Override
    public void delete(Long ssrid) {
        log.info("[要删除的自习室id]ssrid:{}", ssrid);
        ssrDao.deleteBySsrid(ssrid);
    }

    @Override
    public void add(Ssr ssr) {
        log.info("[添加自习室信息]ssr:{}", ssr);
        ssrDao.insertSsr(ssr);
    }

    @Override
    public Ssr findBySid(Long ssrid) {
        log.info("[根据id查询自习室信息]ssrid:{}", ssrid);
        return ssrDao.selectBySsrid(ssrid);
    }

    @Override
    public void edit(Ssr ssr) {
        log.info("[编辑自习室信息]ssr:{}", ssr);
        ssrDao.updateSsr(ssr);
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

    @Override
    public void testRabbitmq(String message) {
        log.info("[测试rabbitmq]message:{}", message);
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

//    public void appointmentTest() {
//        Jedis jedis = null;
//        try{
//            jedis = jedisPool.getResource();
//            String key = jedis.get("key");
//            if (null == key) {
//                Ssr ssr = new Ssr(203L, "二楼三号", 1);
//                ObjectMapper objectMapper = new ObjectMapper();
//                String s = objectMapper.writeValueAsString(ssr);
//                jedis.set("000001", s);
//                jedis.expire("000001", 300);
//                log.info("[已存储key]ssr:{}", "key");
//            } else {
//                log.info("[缓存中已有数据]ssr:{}", "key");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }  finally {
//            jedis.close();
//        }
//    }
}
