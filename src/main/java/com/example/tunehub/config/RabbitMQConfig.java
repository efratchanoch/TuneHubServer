package com.example.tunehub.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ integration.
 * This class sets up the messaging infrastructure required to communicate
 * between the Java Backend and the Node.js Notification Microservice.
 */
@Configuration
public class RabbitMQConfig {

    // Reading queue, exchange, and routing key names from application.properties
    @Value("${tunehub.rabbitmq.queue}")
    private String queueName;

    @Value("${tunehub.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${tunehub.rabbitmq.routingkey}")
    private String routingKey;

    /**
     * Defines the Queue where messages will be stored until consumed.
     * @return a durable Queue instance.
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    /**
     * Defines the Topic Exchange for routing messages.
     * Topic exchanges allow routing based on wildcards and specific keys.
     * @return a TopicExchange instance.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    /**
     * Binds the defined Queue to the Exchange using a specific Routing Key.
     * This ensures messages sent to the exchange with this key land in the correct queue.
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * CRITICAL: MessageConverter Bean.
     * By default, Spring uses Java Serialization (binary).
     * This Bean overrides it to use Jackson for JSON serialization.
     * This is mandatory for cross-language compatibility (Java -> Node.js).
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures the RabbitTemplate to use the JSON MessageConverter.
     * The RabbitTemplate is the primary tool used to send messages to RabbitMQ.
     */
    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}