package tech.zlia.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class JMSProducer {
    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER ;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;
    //默认连接地址
    private static final String BROKER_URL = "tcp://127.0.0.1:61614";

    public static void main(String[] args){
        //连接工厂
        ConnectionFactory connectionFactory ;
        //连接
        Connection connection = null;
        //会话
        Session session;
        //消息的目的地
        Destination destination;
        //消息生产者
        MessageProducer messageProducer ;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME , JMSProducer.PASSWORD , JMSProducer.BROKER_URL) ;
        //窗口尺寸,用来约束在异步发送时producer端允许积压的(尚未ACK)的消息的尺寸,且支队异步发送有意义
        //每次发送消息之后,都将会导致memoryUsage尺寸增加,当broker返回ack时,memoryUsage尺寸减少
//        ((ActiveMQConnectionFactory) connectionFactory).setProducerWindowSize(1000);
        try {
            //通过连接工厂获取连接
            connection  =  connectionFactory.createConnection() ;
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            //创建一个名称为Hello World的消息队列
            destination = session.createTopic("hello");
            //创建消息生产者
            messageProducer = session.createProducer(destination);
            //发送消息
            sendMessage(session , messageProducer);
            //提交事务
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  static void sendMessage(Session session , MessageProducer messageProducer) throws  Exception{
        int i = 0 ;
        while (i < 5){
            TextMessage message = session.createTextMessage("发布消息" + i);
            System.out.println("发送消息:activemq 发送消息：" + i);
//            Thread.sleep(10000);
            messageProducer.send(message);
            i++;
        }
    }

}
