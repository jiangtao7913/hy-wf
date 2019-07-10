package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.SnRepository;
import com.hy.wf.api.service.v1.SnService;
import com.hy.wf.common.util.FreemarkerUtils;
import com.hy.wf.entity.Sn;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-18 16:41
 **/
@Service("snServiceImpl")
@Lazy(false)
public class SnServiceImpl implements SnService, InitializingBean {
    @Autowired
    private SnRepository snRepository;

    // UID
    private HiloOptimizer uidHiloOptimizer;
    // 订单
    private HiloOptimizer orderHiloOptimizer;

    // 订单
    private HiloOptimizer moneyHiloOptimizer;

    @Value("${sn.uid.radix}")
    private Long uidRadix;
    @Value("${sn.uid.maxLo}")
    private int uidMaxLo;

    @Value("${sn.order.prefix}")
    private String orderPrefix;
    @Value("${sn.order.maxLo}")
    private int orderMaxLo;

    @Value("${sn.money.prefix}")
    private String moneyMaxLo;


    @Override
    public void afterPropertiesSet() throws Exception {
        uidHiloOptimizer = new HiloOptimizer(Sn.Type.uid, uidRadix, uidMaxLo);
        orderHiloOptimizer = new HiloOptimizer(Sn.Type.order, orderPrefix, orderMaxLo);
        moneyHiloOptimizer = new HiloOptimizer(Sn.Type.order, moneyMaxLo, orderMaxLo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String generate(Sn.Type type) {
        String sn = null;
        switch (type) {
            case uid:
                sn = uidHiloOptimizer.generate();
                break;
            case order:
                sn = orderHiloOptimizer.generate();
            case money:
                sn = moneyHiloOptimizer.generate();
            default:
                break;
        }

        return sn;
    }

    /**
     * 高低位算法
     */
    private class HiloOptimizer {

        private Sn.Type type;
        // 前缀
        private String prefix;
        // 基数
        private Long radix;
        // 最大低位
        private int maxLo;
        private int lo;
        // 高位
        private long hi;
        // 表示hi位的进位次数
        private long lastValue;

        public HiloOptimizer(Sn.Type type, String prefix, int maxLo) {
            this.type = type;
            this.prefix = prefix != null ? prefix.replace("{", "${") : "";
            this.maxLo = maxLo;
            // 低位初始化，重新初始化的时候进入下面的判断方法
            this.lo = maxLo + 1;
        }

        public HiloOptimizer(Sn.Type type, Long radix, int maxLo) {
            this.type = type;
            this.radix = radix;
            this.maxLo = maxLo;
            // 低位初始化，重新初始化的时候进入下面的判断方法
            this.lo = maxLo + 1;
        }

        public synchronized String generate() {
            if (lo > maxLo) {
                // 表示hi位的进位次数，从数据库id管理表获取 自增并更新到数据库中
                lastValue = getLastValue(type);
                lo = lastValue == 0 ? 1 : 0;
                //高位进位
                hi = lastValue * (maxLo + 1);
            }

            if (StringUtils.isNotEmpty(prefix)) {
                try {
                    String firstPrefix = "";
                    String secondPrefix = prefix;
                    int index = prefix.indexOf("$");
                    if (index > 0) {
                        firstPrefix = prefix.substring(0, index);
                        secondPrefix = prefix.substring(index);
                    }
                    return firstPrefix + FreemarkerUtils.process(secondPrefix, null) + (hi + lo++);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return String.valueOf(hi + lo++);
            } else {
                return String.valueOf(radix + hi + lo++);
            }
        }
    }

    /**
     * 获取末值
     *
     * @param type
     * @return
     */
    private long getLastValue(Sn.Type type) {
        Sn sn = snRepository.findByType(type);
        long lastValue = sn.getLastValue();
        sn.setLastValue(lastValue + 1);
        sn.setModifyDate(new Date());
        snRepository.updateById(sn);

        return lastValue;
    }
}
