package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.omg.CORBA.TIMEOUT;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Sakura on 2019/6/7.
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(long seckillId){
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                //并没有实现内部序列化操作
                //get->byte[] ->反序列化 ->object（seckill）
                //采用自定义序列化
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    //创建空对象
                    Seckill seckill = schema.newMessage();
                    //schema将bytes数据传到seckill中，seckill完成赋值
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    //seckill 被反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckillId(Seckill seckill){
        //object(seckill)->序列化 ->byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckill.getSeckillId();
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //setex:超时缓存
                int timeout = 60*60;//1 hour
                String result = jedis.setex(key.getBytes(), timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
