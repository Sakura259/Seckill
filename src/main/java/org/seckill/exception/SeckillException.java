package org.seckill.exception;

/**
 * Created by Sakura on 2019/6/4.
 */
public class SeckillException  extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
