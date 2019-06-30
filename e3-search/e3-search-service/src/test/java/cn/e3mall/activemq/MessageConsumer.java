package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageConsumer {
	
	@Test
	public void msgConsumer() throws Exception {
		ApplicationContext ApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		System.in.read();
	}
}
