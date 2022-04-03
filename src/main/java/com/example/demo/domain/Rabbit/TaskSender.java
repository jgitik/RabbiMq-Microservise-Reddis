package com.example.demo.domain.Rabbit;


import com.example.demo.domain.Task;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskSender {

    RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendTask(Task task) {
        rabbitMessagingTemplate.convertAndSend(TaskListener.TASK_EXCHANGE, null, task);
    }

}
