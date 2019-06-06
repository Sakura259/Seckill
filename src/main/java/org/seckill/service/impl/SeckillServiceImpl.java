package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Sakura on 2019/6/4.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;
    //md5盐值字符串，用于混淆MD5
    private final String slat = "sahsakjhsalk@#$^$654&%$45632GFHjhLKJKL:GD";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化；超时的基础上维护一致性
        //1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            //访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill !=null){
                //放入redis
                redisDao.putSeckillId(seckill);
            }else {
                return new Exposer(false,seckillId);
            }
        }
        System.out.println(seckill.toString());

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()||nowTime.getTime() >endTime.getTime())
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());

        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
    private String getMD5(long seckillId){
        String base = seckillId + "/" +slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /*
    * 使用注解控制事务方法的优点：
    * 1.开发团队达成一致约定，明确标注事务方法的编程风格
    * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作
    * 3.不是所有方法都需要事务，只有一条修改操作（增删改），只读操作不需要事务控制
    * */
    public SeckillExecution executeSecill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !getMD5(seckillId).equals(md5)) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存，记录秒杀行为
        Date nowTime = new Date();
        try {
            //减库存
            int updataCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updataCount <= 0) {
                throw new SeckillCloseException("seckill is close");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0)
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1){
            throw  e1;
        } catch (RepeatKillException e2){
            throw e2;
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            //转化为运行期异常
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }

}
