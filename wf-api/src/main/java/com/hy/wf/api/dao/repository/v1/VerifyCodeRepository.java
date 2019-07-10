package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.VerifyCode;
import com.hy.wf.entity.base.BaseEntity;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:33
 **/
public interface VerifyCodeRepository extends BaseRepository<VerifyCode,Long> {
    /**
     * 查找最近的验证码
     *
     * @param mobile
     * @param code
     * @param time
     * @param dataStatus
     * @return
     */
    VerifyCode findByMobileCodeAndTime(String mobile, String code, Long time, BaseEntity.DataStatus dataStatus);

    int update(Long id,BaseEntity.DataStatus dataStatus);
}
