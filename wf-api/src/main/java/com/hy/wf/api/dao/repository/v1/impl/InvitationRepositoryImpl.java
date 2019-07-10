package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.InvitationRepository;
import com.hy.wf.entity.Invitation;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-12 16:36
 **/
@Repository
public class InvitationRepositoryImpl extends BaseRepositoryImpl<Invitation,Long> implements InvitationRepository {
    private static final String TABLE_NAME = "t_invitation";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Invitation> getBeanClass() {
        return Invitation.class;
    }


    @Override
    public Invitation findByMasterId(Long userId) {
        return select().where(Condition.equal("master_id",userId)).comm().findOne();
    }

    @Override
    public Invitation findByMasterUid(String masterUid) {
        return select().where(Condition.equal("master_uid",masterUid)).comm().findOne();
    }

    @Override
    public int updateCountById(int count, Long id) {
        return update().set("count",count).where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateIncomeById(Long id, BigDecimal incomeTotal, BigDecimal stairIncome, BigDecimal secondIncome) {
        return update().set("income_total",incomeTotal).set("stair_income",stairIncome).set("second_income",secondIncome).
                where(Condition.equal("id",id)).update();
    }

    @Override
    public Invitation findByIdForUpdate(Long id) {
        return select().where(Condition.equal("id",id)).lock(true).findOne();
    }

    @Override
    public List<Invitation> findOrderIncomeTotal(Integer limit) {
        return select().comm().orderBy("income_total",false).limit(limit).find();
    }
}
