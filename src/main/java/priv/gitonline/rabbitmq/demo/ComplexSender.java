package priv.gitonline.rabbitmq.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

@Controller
public class ComplexSender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static Logger log = LoggerFactory.getLogger(ComplexSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ComplexSender(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
        //这是是设置回调能收到发送到响应
        rabbitTemplate.setConfirmCallback(this);
        //如果设置备份队列则不起作用
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);

    }

    @RequestMapping("/send")
    @ResponseBody
    public void send() {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String sendMsg = "hello1 " + new Date();
        System.out.println("Sender : " + sendMsg);
        //convertAndSend(exchange:交换机名称,routingKey:路由关键字,object:发送的消息内容,correlationData:消息ID)
        rabbitTemplate.convertAndSend("TestDirectExchange","TestDirectRouting1", sendMsg,correlationId);
    }

    //回调确认
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
        }else{
            log.info("消息发送失败:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
        }
    }

    //消息发送到转换器的时候没有对列,配置了备份对列该回调则不生效
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
    }


}
