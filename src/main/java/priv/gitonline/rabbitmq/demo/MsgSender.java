package priv.gitonline.rabbitmq.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.gitonline.rabbitmq.config.DelayQueueDirectConfig;
import priv.gitonline.rabbitmq.config.DelayQueueTopicConfig;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MsgSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public String send(){
        //普通 direct模式
//        rabbitTemplate.convertAndSend(DelayQueueConfig.TTL_QUEUE,"DEMO_"+uid());
        //direct 延迟队列
//        rabbitTemplate.convertAndSend(DelayQueueConfig.TTL_EXCHANGE,DelayQueueConfig.TTL_ROUTING_KEY,"DEMO_"+uid());
        //topic 延迟队列
        rabbitTemplate.convertAndSend(DelayQueueTopicConfig.TOPIC_TTL_EXCHANGE,"topic.ttl.queue.demo.key.666","DEMO_"+uid());
        System.out.println(LocalDateTime.now().toString());
        return "success";
    }

    public String uid(){
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }
}
