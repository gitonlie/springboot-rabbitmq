package priv.gitonline.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 延迟队列
 * 使用ttl和dlx实现
 * Direct模式
 */
@Configuration
public class DelayQueueDirectConfig {

    //-------------Direct模式--------------------------------

    public final static String TTL_QUEUE = "ttl.queue.demo";

    public final static String TTL_ROUTING_KEY = "ttl.queue.demo.key";

    public final static String TTL_EXCHANGE = "ttl.queue.demo.exchange";

    public final static String DLX_QUEUE = "dlx.queue.demo";

    public final static String DLX_ROUTING_KEY = "dlx.queue.demo.key";

    public final static String DLX_EXCHANGE = "dlx.queue.demo.exchange";

    public final static int QUEUE_EXPIRATION = 3000;

    @Bean
    Queue ttlQueue() {
        return QueueBuilder.durable(TTL_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE) // DLX
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY) // dead letter携带的routing key
                .withArgument("x-message-ttl", QUEUE_EXPIRATION) // 设置队列的过期时间
                .build();
    }

    @Bean
    Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE)
                .build();
    }

    @Bean
    DirectExchange ttlExchange() {
        return new DirectExchange(TTL_EXCHANGE);
    }

    @Bean
    DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    Binding dlxBinding(Queue dlxQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue)
                .to(dlxExchange)
                .with(DLX_ROUTING_KEY);
    }

    @Bean
    Binding ttlBinding(Queue ttlQueue, DirectExchange ttlExchange) {
        return BindingBuilder.bind(ttlQueue)
                .to(ttlExchange)
                .with(TTL_ROUTING_KEY);
    }

}
