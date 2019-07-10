package com.hy.wf.api.service.v1;

import com.hy.wf.entity.Sn;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-18 16:40
 **/

public interface SnService {
    /**
     * 生成序列号
     *
     * @param type 类型
     * @return 序列号
     */
    String generate(Sn.Type type);
}
