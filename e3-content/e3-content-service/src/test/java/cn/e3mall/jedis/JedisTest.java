package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	@Test
	public void testJedis() throws Exception {
		//创建连接Jedis对象,参数:host、port
		Jedis jedis = new Jedis("192.168.0.104", 6379);
		jedis.set("gaokao", "20190607");
		String str = jedis.get("gaokao");
		System.out.println(str);
		jedis.close();
	}
	
	@Test
	public void testJedisPool() throws Exception {
		//创建连接池JedisPool对象,参数:host、port
		JedisPool jedisPool = new JedisPool("192.168.0.104", 6379);
		Jedis jedis = jedisPool.getResource();
		String str = jedis.get("gaokao");
		System.out.println(str);
		jedis.close();
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception {
		//创建JedisCluster对象,参数:nodes
		//nodes:set类型,包含若干个HostAndPort对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.0.104", 7001));
		nodes.add(new HostAndPort("192.168.0.104", 7002));
		nodes.add(new HostAndPort("192.168.0.104", 7003));
		nodes.add(new HostAndPort("192.168.0.104", 7004));
		nodes.add(new HostAndPort("192.168.0.104", 7005));
		nodes.add(new HostAndPort("192.168.0.104", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("yuwen", "语文");
		String str = jedisCluster.get("yuwen");
		System.out.println(str);
		jedisCluster.close();
	}

}
