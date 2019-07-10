package com.hy.wf.api.dao.base;

import org.springframework.jdbc.support.KeyHolder;

import java.util.Collection;
import java.util.List;

public interface BaseRepository<T, K> {
    int save(T t, KeyHolder keyHolder);

    T findById(K id);

    List<T> findInIds(Collection<? extends K> ids);

    List<T> findNotInIds(Collection<? extends K> ids);

    int deleteById(K id);

    int totalCount();

    default int save(T t) {
        return save(t, null);
    }


    List<T> findAll();

    void batchSave(List<T> list);

    void batchUpdate(List<T> list);


}
