package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.VipRepository;
import com.hy.wf.api.service.v1.VipService;
import com.hy.wf.entity.Vip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:22
 **/
@Service
public class VipServiceImpl implements VipService {
    @Autowired
    private VipRepository vipRepository;

    @Override
    public Vip findVipById(Long id) {
        return vipRepository.findById(id);
    }

    @Override
    public List<Vip> findList(String appType) {
        return vipRepository.findList(appType);
    }

}
