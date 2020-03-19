package com.yanghui.ssrms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanglubin
 * @description  分页实体类
 * @date 2019/9/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SsrPageResult implements Serializable {
    private Long total;
    private List rows;
    private List optionalDay;
    private List optionalTime;
}
