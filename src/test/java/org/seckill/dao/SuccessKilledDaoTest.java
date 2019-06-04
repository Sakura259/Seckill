package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int i = successKilledDao.insertSuccessKilled(1001L,18681298572L);
        System.out.println(i);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1001L,18681298572L);
        System.out.println(successKilled.toString());
    }
}