package com.msc.ms.authentification.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class QueueConfiguration {
    @Value("#{'${msc.queues}'.split(',')}")
    private List<String> queues;

    private final RabbitAdmin rabbitAdmin;

    QueueConfiguration(final RabbitAdmin pRabbitAdmin) {
        rabbitAdmin = pRabbitAdmin;
    }

    @PostConstruct
    public void postConstruct() {
        this.queues.forEach(queue -> {
            if (rabbitAdmin.getQueueProperties(queue) != null) {
                log.info("Queue {} already exists", queue);
            } else {
                rabbitAdmin.declareQueue(new Queue(queue));
                log.info("Queue '{}' created ", queue);
            }
        });
    }

}
