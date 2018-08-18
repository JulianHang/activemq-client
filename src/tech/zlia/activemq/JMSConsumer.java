package tech.zlia.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import javax.jms.*;

public class JMSConsumer {

    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKER_URL = "tcp://127.0.0.1:61614";

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection;
        //会话,接收或者发送消息的线程
        Session session;
        //消息目的地
        Destination destination;
        //消息的消费者
        MessageConsumer messageConsumer;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.BROKER_URL);

        try {
            //通过工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建一个连接Hello World!的消息
            destination = session.createTopic("hello");
            //创建消息的消费者
            messageConsumer = session.createConsumer(destination);
            while(true){
                //死循环，不断询问服务器是否有消息
                TextMessage textMessage = (TextMessage) messageConsumer.receive(100000);
                if (textMessage != null) {
                    System.err.println("收到的消息:" + textMessage.getText());
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}