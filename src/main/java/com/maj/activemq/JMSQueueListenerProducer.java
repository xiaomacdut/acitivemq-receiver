package com.maj.activemq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSQueueListenerProducer{
    
    public static void main(String [] args){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.11.127:61616");
        Connection connection = null;
        try{
            
            connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地
            Destination destination = session.createQueue("myQueue");
            // 创建发送者
            MessageConsumer consumer = session.createConsumer(destination);
            
            
            MessageListener messageListener = new MessageListener(){
                
                @Override
                public void onMessage(Message message){
                    try{
                        System.out.println(((TextMessage) message).getText());
                    }catch(JMSException e){
                        e.printStackTrace();
                    }
                }
            };
            
            consumer.setMessageListener(messageListener);
            session.commit();
            
            System.in.read();
            
            
            TextMessage textMessage = (TextMessage) consumer.receive();
            System.out.println(textMessage.getText());
            
            // session.commit();
            // session.close();
        }catch(JMSException e){
            e.printStackTrace();
        }catch(IOException e){
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
