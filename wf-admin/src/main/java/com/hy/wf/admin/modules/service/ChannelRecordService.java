package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.ChannelRecord;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:23
 **/
public interface ChannelRecordService extends IService<ChannelRecord> {

    PageUtils queryPage(Map<String, Object> params);

}
