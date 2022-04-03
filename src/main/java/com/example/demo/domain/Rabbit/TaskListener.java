package com.example.demo.domain.Rabbit;


import com.example.demo.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskListener {
    @Value("${server.port}")
    Integer port;

    ObjectMapper objectMapper = new ObjectMapper();

    public static final String TASK_QUEUE = "task.queue";
    public static final String TASK_EXCHANGE = "task.exchange";

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(TASK_QUEUE),
            exchange = @Exchange(TASK_EXCHANGE))
    )
    public void handleTask(Task task) {

        Thread.sleep(1000);
        log.info(String.format("Service \"%s\" start doing task \"%s\" from service \"%s\"",
                port,
                task.getId(),
                task.getPort()));
        log.info(objectMapper.writeValueAsString(task));
    }


}
