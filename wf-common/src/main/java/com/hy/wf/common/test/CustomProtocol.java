package com.hy.wf.common.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-04-16 14:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomProtocol implements Serializable {

    private static final long serialVersionUID = 4583366043598445115L;
    private Long id;
    private String content ;
}
