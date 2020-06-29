package priv.gitonline.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 延迟队列
 * 使用ttl和dlx实现
 * Topic模式
 */
@Configuration
public class DelayQueueTopicConfig {

    //--------------Topic模式

    public final static String TOPIC_TTL_QUEUE = "topic.ttl.queue.demo";

    public final static String TOPIC_TTL_ROUTING_KEY = "topic.ttl.queue.demo.key.*";

    public final static String TOPIC_TTL_EXCHANGE = "topic.ttl.queue.demo.exchange";

    public final static String TOPIC_DLX_QUEUE = "topic.dlx.queue.demo";

    public final static String TOPIC_DLX_ROUTING_KEY = "topic.dlx.queue.demo.key.*";

    public final static String TOPIC_DLX_EXCHANGE = "topic.dlx.queue.demo.exchange";

    public final static int QUEUE_EXPIRATION = 3000;

    @Bean
    Queue topicTtlQueue() {
        return QueueBuilder.durable(TOPIC_TTL_QUEUE)
                .withArgument("x-dead-letter-exchange", TOPIC_DLX_EXCHANGE) // DLX
                .withArgument("x-dead-letter-routing-key", TOPIC_DLX_ROUTING_KEY) // dead letter携带的routing key
                .withArgument("x-message-ttl", QUEUE_EXPIRATION) // 设置队列的过期时间
                .build();
    }

    @Bean
    Queue topicDlxQueue() {
        return QueueBuilder.durable(TOPIC_DLX_QUEUE)
                .build();
    }

    @Bean
    TopicExchange topicTtlExchange() {
        return new TopicExchange(TOPIC_TTL_EXCHANGE);
    }

    @Bean
    TopicExchange topicDlxExchange() {
        return new TopicExchange(TOPIC_DLX_EXCHANGE);
    }

    @Bean
    Binding topicDlxBinding(Queue topicDlxQueue, TopicExchange topicDlxExchange) {
        return BindingBuilder.bind(topicDlxQueue)
                .to(topicDlxExchange)
                .with(TOPIC_DLX_ROUTING_KEY);
    }

    @Bean
    Binding topicTtlBinding(Queue topicTtlQueue, TopicExchange topicTtlExchange) {
        return BindingBuilder.bind(topicTtlQueue)
                .to(topicTtlExchange)
                .with(TOPIC_TTL_ROUTING_KEY);
    }
}
