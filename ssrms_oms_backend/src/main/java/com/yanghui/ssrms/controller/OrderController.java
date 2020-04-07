package com.yanghui.ssrms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yanghui.ssrms.common.MessageConst;
import com.yanghui.ssrms.entity.*;
import com.yanghui.ssrms.pojo.Order;
import com.yanghui.ssrms.pojo.Ssr;
import com.yanghui.ssrms.service.OrderService;
import com.yanghui.ssrms.service.SsrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Reference
    private OrderService orderService;

    @GetMapping("/studentFindPage")
    @PreAuthorize("hasAuthority('ORDER_QUERY')")
    public PageResult studentFindPage(QueryPageBean queryPageBean) {
        String username = getUsername();
//        String username = "20161564203";
        log.info("[学生端订单信息-分页查询]data:{}", queryPageBean);
        try {
            return orderService.StudentPageQuery(queryPageBean, username);
        } catch (RuntimeException e) {
            log.error("[学生端订单信息-分页查询]异常", e);
            return new PageResult(0L, Collections.emptyList());
        }
    }

    @GetMapping("/adminFindPage")
    @PreAuthorize("hasAuthority('ADMIN_ORDER_QUERY')")
    public PageResult adminFindPage(AdminQueryPageBean adminQueryPageBean) {
        log.info("[管理员端订单信息-分页查询]data:{}", adminQueryPageBean);
        try {
            return orderService.adminPageQuery(adminQueryPageBean);
        } catch (RuntimeException e) {
            log.error("[管理员端订单信息-分页查询]异常", e);
            return new PageResult(0L, Collections.emptyList());
        }
    }

    @GetMapping("/findById")
    @PreAuthorize("hasAuthority('ORDER_QUERY')")
    public Result findById(Long id) {
        log.info("[订单信息-根据学生id查询]id:{}", id);
        try {
            Order order = orderService.findById(id);
            return new Result(true, MessageConst.QUERY_ORDER_SUCCESS, order);
        } catch (RuntimeException e) {
            log.error("[订单信息-分页查询]异常", e);
            return new Result(false, MessageConst.QUERY_ORDER_FAIL);
        }
    }

    @DeleteMapping("/cancel")
    @PreAuthorize("hasAuthority('ORDER_CANCEL')")
    public Result cancel(Long id) {
        log.debug("[要取消的预约订单id]id:{}", id);
        try {
            orderService.cancel(id);
            return new Result(true, MessageConst.CANCEL_ORDER_SUCCESS);
        } catch (RuntimeException e) {
            log.error("[要取消的预约订单id-删除]异常", e);
            return new Result(false, MessageConst.CANCEL_ORDER_FAIL);
        }
    }


    @PutMapping("/modify")
    @PreAuthorize("hasAnyAuthority('ORDER_MODIFY')")
    public Result modify(@RequestBody Order order) {
        log.debug("[修改预约订单信息]order:{}", order);
        try {
            orderService.modify(order);
            return new Result(true, MessageConst.MODIFY_ORDER_SUCCESS);
        } catch (RuntimeException e) {
            log.error("[修改预约订单信息-重置]异常", e);
            return new Result(false, MessageConst.MODIFY_ORDER_FAIL);
        }
    }


    public String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (null != principal && principal instanceof User) {
            User user = (User) principal;
            return user.getUsername();
        }
        return null;
    }
}
