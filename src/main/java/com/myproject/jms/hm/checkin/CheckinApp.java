package com.bharath.jms.hm.checkin;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hm.model.Passenger;

public class CheckinApp {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();

			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			Passenger passenger = new Passenger();
			passenger.setId(123);
			passenger.setFirstName("Bob");
			passenger.setLastName("Blue Cross Blue Shield");
			passenger.setEmail("a@a.com");
			passenger.setPhone(500d);
			objectMessage.setObject(passenger);

			for (int i = 1; i <= 10; i++) {
				producer.send(requestQueue, objectMessage);
			}

			JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
			MapMessage replyMessage = (MapMessage) consumer.receive(30000);
			System.out.println("Passenger reservation done:" + replyMessage.getInt("id"));

		}

		;

	}

}
