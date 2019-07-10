package com.hy.wf.api.dao.repository.Test;

import com.hy.wf.api.dao.base.BaseRepository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-05 14:27
 **/
public interface TestRepository extends BaseRepository<User,Long> {
    User test(CharSequence sql, Object... args);
}
