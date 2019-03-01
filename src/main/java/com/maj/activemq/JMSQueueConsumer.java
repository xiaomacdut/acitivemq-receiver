package com.maj.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消费队列消息
 * @author xiaomacdut
 * @date 2019年3月1日 下午9:47:51
 */
public class JMSQueueConsumer{
    
    public static void main(String [] args){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.11.127:61616");
        Connection connection = null;
        try{
            
            connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(Boolean.FALSE, Session.DUPS_OK_ACKNOWLEDGE); // 延迟确认
            // 创建目的地
            Destination destination = session.createQueue("myQueue");
            // 创建发送者
            MessageConsumer consumer = session.createConsumer(destination);
            for(int i = 0; i < 10; i++){
                TextMessage textMessage = (TextMessage) consumer.receive();
                System.out.println(textMessage.getText());
                if(i == 8){
                    textMessage.acknowledge();
                }
            }
            // session.commit();//表示消息被自动确认
            session.close();
        }catch(JMSException e){
            e.printStackTrace();
        }finally{
            if(connection != null){
                try{
                    connection.close();
                }catch(JMSException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
