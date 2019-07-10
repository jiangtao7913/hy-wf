package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.UserRepository;
import com.hy.wf.entity.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 17:59
 **/
@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User,Long> implements UserRepository {
    private static final String TABLE_NAME = "t_user";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<User> getBeanClass() {
        return User.class;
    }

    @Override
    public User findByUid(String uid) {
        return select().where(Condition.equal("uid",uid)).comm().findOne();
    }

    @Override
    public User findByHardKeyAndType(String hardwareKey, User.AccountType accountType) {
        return select().where(Condition.equal("hardware_key",hardwareKey)).where(Condition.equal("account_type",accountType.value)).comm().findOne();
    }

    @Override
    public User findByAccountBindTypeAndSign(User.AccountType accountType, String sign,String appType) {
        return select().where(Condition.equal("account_type",accountType.value)).where(Condition.equal("sign",sign)).where(Condition.equal("app_type",appType)).findOne();
    }

    @Override
    public int updatePasswordById(Long id, String password) {
        return update().set("password",password).where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateHardwareKeyAndSourceById(String hardwareKey, Long id,String source) {
        return update().set("hardware_key",hardwareKey).set("modify_date",new Date()).where(Condition.equal("id",id))
                .set("source",source).update();
    }

    @Override
    public int updateOnlineById(User.Online online, Long id) {
        return update().set("online",online.value).set("modify_date",new Date()).where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateInfo(Long id, String column, String value) {
        return update().set(column,value).where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateRechargeTotalAndBalance(Long id,BigDecimal rechargeTotal,BigDecimal balance) {
        return update().set("recharge_total",rechargeTotal).set("balance",balance).
                set("modify_date",new Date()).where(Condition.equal("id",id)).update();
    }

    @Override
    public User findByIdForUpdate(Long id) {
        return select().where(Condition.equal("id",id)).lock(true).findOne();
    }

    @Override
    public int updateBalanceById(Long id, BigDecimal balance) {
        return update().set("balance",balance).where(Condition.equal("id",id)).update();
    }
}
