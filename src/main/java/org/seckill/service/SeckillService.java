package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在"使用者"角度设计接口
 * 三个方面;方法定义粒度，参数，返回类型
 * Created by Sakura on 2019/6/4.
 */
public interface SeckillService {
    /*
    * 1.查询所有秒杀记录
    * */
    public List<Seckill> getSeckillList();

    //2.查询单个秒杀记录
    public Seckill getById(long seckillId);

    //3.秒杀开启时，输出秒杀接口的地址，否则输出系统时间和秒杀时间
    public Exposer exportSeckillUrl(long seckillId);

    //4.执行秒杀操作
    public SeckillExecution executeSecill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}
