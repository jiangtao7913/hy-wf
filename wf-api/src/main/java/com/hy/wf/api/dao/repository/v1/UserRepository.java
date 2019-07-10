package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 17:58
 **/
public interface UserRepository extends BaseRepository<User,Long> {
    /**
     * 根据uid查询用户
     * @param uid
     * @return
     */
    User findByUid(String uid);

    /**
     * 根据手机标识和类型查询用户
     * @param hardwareKey
     * @param
     * @return
     */
    User findByHardKeyAndType(String hardwareKey, User.AccountType accountType);

    /**
     * 根据手机号查看用户
     * @param accountType
     * @param sign
     * @return
     */
    User findByAccountBindTypeAndSign(User.AccountType accountType,String sign,String appType);

    /** 
    * @Description: 修改密码
    * @Param [id, password]
    * @return: int 
    * @Author: jt 
    * @Date: 2019/2/27 
    */ 
    int updatePasswordById(Long id,String password);

    int updateHardwareKeyAndSourceById(String hardwareKey, Long id,String source);

    int updateOnlineById(User.Online online, Long id);

    /** 
    * @Description: 根据列修改用户信息 
    * @Param [id, column, value]
    * @return: int 
    * @Author: jt 
    * @Date: 2019/2/28 
    */ 
    int updateInfo(Long id,String column,String value);


    /** 
    * @Description: 修改用户余额和充值金额
    * @Param [id, rechargeTotal, balance]
    * @return: int 
    * @Author: jt 
    * @Date: 2019/3/4 
    */ 
    int updateRechargeTotalAndBalance(Long id,BigDecimal rechargeTotal,BigDecimal balance);

    /** 
    * @Description: 锁住某行数据
    * @Param [id]
    * @return: com.hy.wf.entity.User 
    * @Author: jt 
    * @Date: 2019/3/7
    */ 
    User findByIdForUpdate(Long id);
    
    /** 
    * @Description: 修改余额 
    * @Param [id, balance]
    * @return: int 
    * @Author: jt 
    * @Date: 2019/3/13 
    */ 
    int updateBalanceById(Long id,BigDecimal balance);

}
