package com.msc.ms.authentification.message.consumer;

import com.msc.ms.authentification.message.model.PasswordProccessDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthConsumer {

@RabbitListener(queues = "users.passwords")
    void createPasswordRecords(PasswordProccessDTO pPasswordProccessDTO){


}

}
