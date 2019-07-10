package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.base.SelectBuilder;
import com.hy.wf.api.dao.repository.v1.InvitationDetailRepository;
import com.hy.wf.entity.InvitationDetail;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-12 17:17
 **/
@Repository
public class InvitationDetailRepositoryImpl extends BaseRepositoryImpl<InvitationDetail,Long> implements InvitationDetailRepository {
    private static final String TABLE_NAME = "t_invitation_detail";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<InvitationDetail> getBeanClass() {
        return InvitationDetail.class;
    }

    @Override
    public InvitationDetail finByPreticeUidAndType(String preticeUid, InvitationDetail.Type type) {
        return select().where(Condition.equal("pretice_uid",preticeUid)).where(Condition.equal("type",type.value)).comm().findOne();
    }

    @Override
    public InvitationDetail findByInvitationIdAndPreticeId(Long invitationId, Long preticeId) {
        return select().where(Condition.equal("invitation_id",invitationId)).where(Condition.equal("pretice_id",preticeId)).comm().findOne();
    }

    @Override
    public List<InvitationDetail> findByInvitationIdAndType(Long invitationId,InvitationDetail.Type type,Integer offset,Integer limit) {
        SelectBuilder<InvitationDetail,Long> selectBuilder =   select().where(Condition.equal("invitation_id",invitationId)).where(Condition.equal("type",type.value));
        if(0 != offset){
            selectBuilder.where(Condition.lessThen("id",offset));
        }
        return selectBuilder.comm().limit(limit).orderBy("id",false).find();
    }

    @Override
    public int updateIncomeTotalById(Long id, BigDecimal incomeTotal) {
        return update().set("income_total",incomeTotal).where(Condition.equal("id",id)).update();
    }

    @Override
    public InvitationDetail finByPreticeUidAndTypeForUpdate(String preticeUid, InvitationDetail.Type type) {
        return select().where(Condition.equal("pretice_uid",preticeUid)).where(Condition.equal("type",type.value)).comm().lock(true).findOne();

    }

}
