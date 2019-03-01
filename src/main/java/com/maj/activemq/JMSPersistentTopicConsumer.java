package com.maj.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSPersistentTopicConsumer{
    
    public static void main(String [] args){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.11.127:61616");
        Connection connection = null;
        try{
            
            connection = connectionFactory.createConnection();
            connection.setClientID("Mic-001");
            
            connection.start();
            
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地
            Topic destination = session.createTopic("myTopic");
            // 创建发送者
            MessageConsumer consumer = session.createDurableSubscriber(destination, "Mic-001");
            
            TextMessage textMessage = (TextMessage) consumer.receive();
            System.out.println(textMessage.getText());
            
            // session.commit(); //消息被确认
            
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
