package com.yanghui.ssrms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ssr implements Serializable {
    private Long ssrid;
    private String ssrname;
    private int state;
}
