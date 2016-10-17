package com.disp.amqm;

import java.util.Map;

import javax.sql.DataSource;

import java.util.Map;

import javax.jms.Queue;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;






import com.disp.messageprocessing.JmsMessageSender;

public class Producer implements Runnable {
    public void run() {
	    // init spring context
	    ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");
	    // get bean from context
	    JmsMessageSender jmsMessageSender = (JmsMessageSender)ctx.getBean("jmsMessageSender");
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("dataSourceApplicationContext.xml");
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		JndiContext jndiContext = null;
		try {
			jndiContext = new JndiContext();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jndiContext.bind("dataSource", dataSource);
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CamelContext camelContext = new DefaultCamelContext(jndiContext);
		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("timer://pollTable"
							+ "?period=3s")
                    .setBody(constant("select * from signalement"))
                    .to("jdbc:dataSource")
                    .split(simple("${body}"))
                    .process(new Processor() {
						
						public void process(Exchange exchange) throws Exception {
							Map<String, Object> signalement = exchange.getIn().getBody(Map.class);
							System.out.println("Process signalement " + signalement);
							 // send to default destination 
						    jmsMessageSender.send(signalement); 
						    // close spring application context
						    ((ClassPathXmlApplicationContext)ctx).close();
						}
					});
				}
			});
			camelContext.start();
			Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				camelContext.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			context.close();
		}
    
    }
    }