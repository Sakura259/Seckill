package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Sakura on 2019/6/4.
 */
public interface SeckillDao {
    /*
    * 减库存
    * @param：用于传递参数（当存在多个参数传递时，绑定参数）
    * */
    public int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    public Seckill queryById(long seckillId);

    public List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

    //使用存储过程执行秒杀
    public void killByProcedure(Map<String ,Object> paramMap);
}
