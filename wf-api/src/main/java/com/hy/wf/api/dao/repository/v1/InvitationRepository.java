package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Invitation;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-12 16:36
 **/
public interface InvitationRepository extends BaseRepository<Invitation,Long> {
    
    Invitation findByMasterId(Long userId);

    /** 
    * @Description: 通过邀请码查询
    * @Param [masterUid]
    * @return: com.hy.wf.entity.Invitation 
    * @Author: jt 
    * @Date: 2019/3/12 
    */ 
    Invitation findByMasterUid(String masterUid);

    /**
     * 修改邀请人数
     */
    int updateCountById(int count,Long id);

    /**
     * 修改邀请收益
     */
    int updateIncomeById(Long id, BigDecimal incomeTotal,BigDecimal stairIncome,BigDecimal secondIncome);

    Invitation findByIdForUpdate(Long id);


    List<Invitation> findOrderIncomeTotal(Integer limit);
}
