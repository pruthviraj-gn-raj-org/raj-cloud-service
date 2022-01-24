package com.raj.cloud.service.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationServiceRabbitMQConfig {

	public static String exchange="raj-notification-service";
	
	public static String callQueueName="call";
	public static String callroutingKey="call";	
	
	public static String simpleMailQueueName="simpleMail";	
	public static String simpleMailroutingKey="simpleMail";
	
	
	@Bean
	public TopicExchange exchange()
	{
		return new TopicExchange(exchange);
	}
	
	@Bean
	public Queue callQueue()
	{
		return new Queue(callQueueName);
	}
	
	
	@Bean
	public Binding callBinding(Queue callQueue,TopicExchange exchange)
	{
		return BindingBuilder.bind(callQueue).to(exchange).with(callroutingKey);
	}
	
	@Bean
	public Queue simpleMailQueue()
	{
		return new Queue(simpleMailQueueName);
	}
	
	
	@Bean
	public Binding simpleMailBinding(Queue simpleMailQueue,TopicExchange exchange)
	{
		return BindingBuilder.bind(simpleMailQueue).to(exchange).with(simpleMailroutingKey);
	}
}
