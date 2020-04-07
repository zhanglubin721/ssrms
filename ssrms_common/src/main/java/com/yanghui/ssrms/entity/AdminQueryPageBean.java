package com.yanghui.ssrms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangmeng
 * @description 查询条件封装类
 * @date 2019/9/16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminQueryPageBean implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private String ssr_ssrid;
    private String user_username;
}
