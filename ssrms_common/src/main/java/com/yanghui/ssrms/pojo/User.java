package com.yanghui.ssrms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private int id;
    private String username; // 用户名，唯一
    private String password; // 密码
    private Set<Role> roles = new HashSet<Role>(0);//对应角色集合
}
