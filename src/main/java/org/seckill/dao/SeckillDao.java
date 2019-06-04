package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by Sakura on 2019/6/4.
 */
public interface SeckillDao {
    /*
    * 减库存
    * */
    public int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    public Seckill queryById(long seckillId);

    public List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}
