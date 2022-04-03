package com.example.demo.services;


import com.example.demo.Config.RedisLock;
import com.example.demo.domain.Rabbit.TaskSender;
import com.example.demo.domain.Task;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskInitializierService {
    @Value("${server.port}")
    @NonFinal Integer port;

    RedisLock redisLock;
    TaskSender taskSender;



    public static final long ONE_MINUTES_IN_MILLIS = 1000 * 60;
    public static final String GENERATE_TASKS_KEY = "generate:tasks";

    //fixedDelay гарантирует что предыдущая таска не начнет выполняться пока предыдущая работает,
    //лучше использовать крон
    @Scheduled(cron = "0/5 * * * * *")
    public void generateTasks() {

        if (redisLock.acquireLock(ONE_MINUTES_IN_MILLIS, GENERATE_TASKS_KEY)) {
            redisLock.realeaseLock(GENERATE_TASKS_KEY);
            log.info(Strings.repeat("-", 100));
            log.info(String.format("Server \"%s\" Start Generate Task", port));
            log.info(Strings.repeat("-", 100));
        }
        for (int i = 0; i < 5; i++) {
            taskSender.sendTask(Task.builder()
                    .id(UUID.randomUUID().toString().substring(0, 2))
                    .port(port)
                    .build());
        }

    }

}
