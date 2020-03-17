package com.yanghui.ssrms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yanghui.ssrms.dao.SsrDao;
import com.yanghui.ssrms.entity.PageResult;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Ssr;
import com.yanghui.ssrms.service.SsrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class SsrServiceImpl implements SsrService {

    @Autowired
    private SsrDao ssrDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        log.info("[自习室信息-分页查询]data：{}",queryPageBean);
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 分页查询数据库
        Page<Ssr> page = ssrDao.selectByConditon(queryPageBean.getQueryString());
        log.info("[测试获取查询日期]day:{}", queryPageBean.getDay());
        log.info("[测试获取查询时间段]time:{}", queryPageBean.getTime());
        //封装分页数据
        return new PageResult(page.getTotal(),page.getResult());
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
    public void appointment() {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String key = jedis.get("key");
            if (null == key) {
                Ssr ssr = new Ssr(203L, "二楼三号", 1);
                ObjectMapper objectMapper = new ObjectMapper();
                String s = objectMapper.writeValueAsString(ssr);
                jedis.set("000001", s);
                jedis.expire("000001", 300);
                log.info("[已存储key]ssr:{}", "key");
            } else {
                log.info("[缓存中已有数据]ssr:{}", "key");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }  finally {
            jedis.close();
        }
    }
}
