package cn.chenjianlink.lstore.jedis;

import cn.chenjianlink.lstore.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {
    @Test
    public void jedisClient() throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/springconfig-redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("test", "mytestJedisClient");
        String s = jedisClient.get("test");
        System.out.println(s);
    }
}
