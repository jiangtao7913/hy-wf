package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.InvitationDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-12 17:16
 **/
public interface InvitationDetailRepository extends BaseRepository<InvitationDetail,Long> {

    InvitationDetail finByPreticeUidAndType(String preticeUid,InvitationDetail.Type type);

    InvitationDetail findByInvitationIdAndPreticeId(Long invitationId,Long preticeId);

    List<InvitationDetail> findByInvitationIdAndType(Long invitationId, InvitationDetail.Type type,Integer offset,Integer limit);

    int updateIncomeTotalById(Long id, BigDecimal incomeTotal);

    InvitationDetail finByPreticeUidAndTypeForUpdate(String preticeUid,InvitationDetail.Type type);
}
