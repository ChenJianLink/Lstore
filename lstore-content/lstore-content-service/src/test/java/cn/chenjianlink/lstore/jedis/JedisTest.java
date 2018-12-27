package cn.chenjianlink.lstore.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.LinkedHashSet;
import java.util.Set;

public class JedisTest {

    @Test
    public void testJedis() throws Exception {
        Jedis jedis = new Jedis("192.168.56.101", 6379);
        jedis.configSet("timeout", "30");
        jedis.set("test123", "test");
        String s = jedis.get("test123");
        System.out.println(s);
        jedis.close();
    }

    @Test
    public void testJedisCluster() throws Exception {

        Set<HostAndPort> nodes = new LinkedHashSet<>();
        nodes.add(new HostAndPort("192.168.56.101", 7001));
        nodes.add(new HostAndPort("192.168.56.101", 7002));
        nodes.add(new HostAndPort("192.168.56.101", 7003));
        nodes.add(new HostAndPort("192.168.56.101", 7004));
        nodes.add(new HostAndPort("192.168.56.101", 7005));
        nodes.add(new HostAndPort("192.168.56.101", 7000));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用JedisCluster对象操作redis。
        jedisCluster.set("test", "123");
        String string = jedisCluster.get("test");
        System.out.println(string);
        //关闭JedisCluster对象
        jedisCluster.close();

    }
}
