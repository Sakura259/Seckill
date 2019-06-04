package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by Sakura on 2019/6/4.
 */
public interface SuccessKilledDao {
    /*
    * 插入购买明细，可过滤重复
    * */
    public int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    //根据id查询SuccessKilled并携带秒杀产品对象实体
    public SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

}
