package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.VerifyCodeRepository;
import com.hy.wf.entity.VerifyCode;
import com.hy.wf.entity.base.BaseEntity;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:35
 **/
@Repository
public class VerifyCodeRepositoryImpl extends BaseRepositoryImpl<VerifyCode,Long> implements VerifyCodeRepository {
    private static final String TABLE_NAME = "t_verify_code";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<VerifyCode> getBeanClass() {
        return VerifyCode.class;
    }

    @Override
    public VerifyCode findByMobileCodeAndTime(String mobile, String code, Long time, BaseEntity.DataStatus dataStatus) {
        return select().where(Condition.equal("mobile",mobile)).where(Condition.equal("code",code))
                .where(Condition.greaterThanEqual("expiry_time",time))
                .where(Condition.equal("data_status",dataStatus.value)).findOne();
    }

    @Override
    public int update(Long id, BaseEntity.DataStatus dataStatus) {
        return update().set("data_status",dataStatus.value).where(Condition.equal("id",id)).update();
    }
}
