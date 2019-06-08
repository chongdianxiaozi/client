package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;


public class ActiveMqTest {

	/**
	 * 发送消息:点对点
	 * @throws Exception
	 */
	@Test
	public void testQueueProducer() throws Exception {
		//创建连接工厂对象,指定服务ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.103:61616");
		//使用工厂对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接,调用Connection对象的start方法
		connection.start();
		//创建Session对象
		//参数1:是否开启事务,如果开启事务,则参数2无意义	 参数2:应答模式  自动应答或手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用Session对象创建Destination对象:queue,topic
		Queue queue = session.createQueue("test-queue");
		//使用Session对象创建Producer对象
		MessageProducer producer = session.createProducer(queue);
		//创建Message对象,使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("Hello Activemq");
		textMessage = session.createTextMessage("hello activemq");
		//发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void testQueueConsumer() throws Exception {
		//创建连接工厂对象,指定服务ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.103:61616");
		//使用工厂对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接,调用Connection对象的start方法
		connection.start();
		//创建Session对象
		//参数1:是否开启事务,如果开启事务,则参数2无意义	 参数2:应答模式  自动应答或手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用Session对象创建Destination对象:queue,topic
		Queue queue = session.createQueue("test-queue");
		//使用Session对象创建Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void testTopicProducer() throws Exception {
		//创建连接工厂对象,指定服务ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.103:61616");
		//使用工厂对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接,调用Connection对象的start方法
		connection.start();
		//创建Session对象
		//参数1:是否开启事务,如果开启事务,则参数2无意义	 参数2:应答模式  自动应答或手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用Session对象创建Destination对象:queue,topic
		Topic topic = session.createTopic("test-topic");
		//使用Session对象创建Producer对象
		MessageProducer producer = session.createProducer(topic);
		//创建Message对象,使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("Topic Message");
		textMessage = session.createTextMessage("topic message");
		//发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void testTopicConsumer() throws Exception {
		//创建连接工厂对象,指定服务ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.103:61616");
		//使用工厂对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接,调用Connection对象的start方法
		connection.start();
		//创建Session对象
		//参数1:是否开启事务,如果开启事务,则参数2无意义	 参数2:应答模式  自动应答或手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用Session对象创建Destination对象:queue,topic
		Topic topic = session.createTopic("test-topic");
		//使用Session对象创建Consumer对象
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//System.out.println("topic消费者1启动...");
		//System.out.println("topic消费者2启动...");
		System.out.println("topic消费者3启动...");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
		
	}
	
}
