package com.hy.wf.api.web;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: hy-wf
 * @description: 测试实体类
 * @author: jt
 * @create: 2019-01-04 17:59
 **/
@Data
public class UserTest {
    private Long id;
    @NotNull(message = "姓名不能为空")
    private String name;

}
