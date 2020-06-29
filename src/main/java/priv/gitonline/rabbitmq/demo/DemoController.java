package priv.gitonline.rabbitmq.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    private MsgSender sender;

    @RequestMapping("/enter")
    public String enter(){
        sender.send();
        return "join success!";
    }
}
