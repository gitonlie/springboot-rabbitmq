package priv.gitonline.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.gitonline.rabbitmq.config.DelayQueueDirectConfig;
import priv.gitonline.rabbitmq.config.DelayQueueTopicConfig;
import priv.gitonline.rabbitmq.config.DirectRabbitConfig;

import java.time.LocalDateTime;

@Component
public class MsgReceiver {

    @Autowired
    private AmqpTemplate rabbitTemplate;

//    @RabbitListener(queues= DelayQueueConfig.DLX_QUEUE)
    @RabbitListener(queues= DelayQueueTopicConfig.TOPIC_DLX_QUEUE)
    @RabbitHandler
    public void process(Message message, Channel channel){
        System.out.println(LocalDateTime.now().toString());
    }

    @RabbitListener(queues= "TestDirectQueue")
    @RabbitHandler
    public void process2(Message message, Channel channel){
        System.out.println(message.getBody()+","+message.getMessageProperties());
    }
}
