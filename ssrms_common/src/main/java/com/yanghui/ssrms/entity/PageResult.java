package com.yanghui.ssrms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangmeng
 * @description  分页实体类
 * @date 2019/9/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult implements Serializable {
    private Long total;
    private List rows;
}
