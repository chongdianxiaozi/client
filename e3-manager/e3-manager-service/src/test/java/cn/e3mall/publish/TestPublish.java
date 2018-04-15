package cn.e3mall.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPublish {
	
	@Test
	public void publistService() throws Exception {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		/*while(true) {
			Thread.sleep(1000);
		}*/
		System.out.println("已开启服务------");
		System.in.read();
		System.out.println("已关闭服务------");
	}
}
