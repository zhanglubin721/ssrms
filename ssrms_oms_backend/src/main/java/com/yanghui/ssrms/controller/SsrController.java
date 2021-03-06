package com.yanghui.ssrms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yanghui.ssrms.common.MessageConst;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.entity.Result;
import com.yanghui.ssrms.entity.SsrPageResult;
import com.yanghui.ssrms.pojo.Ssr;
import com.yanghui.ssrms.service.SsrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/ssr")
@Slf4j
public class SsrController {

    @Reference
    private SsrService ssrService;

    @GetMapping("/findPage")
    @PreAuthorize("hasAuthority('SSR_QUERY')")
    public SsrPageResult findPage(QueryPageBean queryPageBean) {
        log.info("[自习室信息-分页查询]data:{}", queryPageBean);
        try {
            return ssrService.pageQuery(queryPageBean);
        } catch (RuntimeException e) {
            log.error("[自习室信息-分页查询]异常", e);
            return new SsrPageResult(0L, Collections.emptyList(), null, null);
        }
    }

    @GetMapping("/findBySsrid")
    @PreAuthorize("hasAuthority('SSR_QUERY')")
    public Result findBySid(Long ssrid) {
        log.info("[自习室信息-根据学生id查询]ssrid:{}", ssrid);
        try {
            Ssr ssr = ssrService.findBySid(ssrid);
            return new Result(true, MessageConst.QUERY_SSR_SUCCESS, ssr);
        } catch (RuntimeException e) {
            log.error("[自习室信息-分页查询]异常", e);
            return new Result(false, MessageConst.QUERY_SSR_FAIL);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SSR_DELETE')")
    public Result delete(Long ssrid) {
        log.debug("[要删除的自习室id]ssrid:{}", ssrid);
        try {
            ssrService.delete(ssrid);
            return new Result(true, MessageConst.DELETE_SSR_SUCCESS);
        } catch (RuntimeException e) {
            log.error("[自习室信息-删除]异常", e);
            return new Result(false, MessageConst.DELETE_SSR_FAIL);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SSR_ADD')")
    public Result add(@RequestBody Ssr ssr) {
        log.info("[自习室信息-新增]ssr:{}", ssr);
        try {
            ssrService.add(ssr);
            return new Result(true, MessageConst.ADD_SSR_SUCCESS);
        } catch (RuntimeException e) {
            log.error("[自习室信息-新增]", e);
            return new Result(false, MessageConst.ADD_SSR_FAIL + "：" + e.getMessage());
        }
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyAuthority('SSR_EDIT')")
    public Result edit(@RequestBody Ssr ssr) {
        log.debug("[修改自习室信息]ssr:{}", ssr);
        try {
            ssrService.edit(ssr);
            return new Result(true, MessageConst.EDIT_SSR_SUCCESS);
        } catch (RuntimeException e) {
            log.error("[自习室信息-重置]异常", e);
            return new Result(false, MessageConst.EDIT_SSR_FAIL);
        }
    }

    @PostMapping("/appointment")
    @PreAuthorize("hasAnyAuthority('SSR_APPOINTMENT')")
    public Result appointment(Long ssrid, String chooseDay, String chooseTime) {
        String username = getUsername();
        try{
            ssrService.appointment(ssrid, chooseDay, chooseTime, username);
            return new Result(true, MessageConst.APPOINTMENT_SSR_SUCCESS);
        } catch (RuntimeException e) {
            log.error("[预约异常]异常", e);
            return new Result(false, MessageConst.APPOINTMENT_SSR_FAIL);
        }
    }

    @GetMapping("/testRabbitmq")
    public void testRabbitmq() {
        ssrService.testRabbitmq("已发送");
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
