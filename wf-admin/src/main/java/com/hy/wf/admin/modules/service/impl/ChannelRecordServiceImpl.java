package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.ChannelRecordDao;
import com.hy.wf.admin.modules.service.ChannelRecordService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.ChannelRecord;
import com.hy.wf.entity.Record;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:24
 **/
@Service
public class ChannelRecordServiceImpl extends ServiceImpl<ChannelRecordDao, ChannelRecord> implements ChannelRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");

        Page<ChannelRecord> page = this.selectPage(
                new Query<ChannelRecord>(params).getPage(),
                new EntityWrapper<ChannelRecord>()
                        .like(StringUtils.isNotBlank(title),"title", title)
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }
}
