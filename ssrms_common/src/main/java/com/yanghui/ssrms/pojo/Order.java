package com.yanghui.ssrms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private Long id;
    private String chooseday;
    private String choosetime;
    private Long user_username;
    private LocalDateTime ordertime;
    private int isfinish;
}
