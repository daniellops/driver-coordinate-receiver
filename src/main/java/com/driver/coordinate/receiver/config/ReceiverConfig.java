package com.driver.coordinate.receiver.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceiverConfig {

    @Value("${events.queue}")
    private String eventsQueueName;

    @Value("${events.dlq}")
    private String eventsDlqName;

    @Value("${events.ex}")
    private String eventsExName;

    @Value("${events.dlx}")
    private String eventsDlxName;

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory conn, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(conn);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue eventsQueue() {
        return QueueBuilder.durable(eventsQueueName)
                .deadLetterExchange(eventsDlxName)
                .build();
    }

    @Bean
    public Queue eventsDlq() {
        return QueueBuilder.durable(eventsDlqName).build();
    }

    @Bean
    FanoutExchange eventsExchange() {
        return ExchangeBuilder.fanoutExchange(eventsExName).build();
    }

    @Bean
    FanoutExchange deadLetterExchange() {
        return ExchangeBuilder.fanoutExchange(eventsDlxName).build();
    }

    @Bean
    public Binding bindEvents() {
        return BindingBuilder.bind(eventsQueue()).to(eventsExchange());
    }

    @Bean
    public Binding bindDlxEvents() {
        return BindingBuilder.bind(eventsDlq()).to(deadLetterExchange());
    }
}