package com.hy.wf.api.dao.repository.Test;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-05 14:26
 **/
@Data
public class User implements Serializable {


    private static final long serialVersionUID = 8552844399818304714L;

    private Long id;

    private String name;

    private int age;

    private int sex;

    private Date createDate;

    private Date modifyDate;
}
