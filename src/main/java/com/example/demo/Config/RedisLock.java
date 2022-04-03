package com.example.demo.Config;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RedisLock {

    RedisTemplate<String, Long> redisTemplate;
    private static final String KEY_TEMPLATE = "lock";

    public boolean acquireLock(Long expiresMilis, String taskKey) {
        String lockKey = getLockKey(taskKey);

        Long expiredAt = redisTemplate.opsForValue().get(lockKey);
        Long currentTimeMillis = System.currentTimeMillis();
        if (Objects.nonNull(expiredAt)) {
            if (expiredAt <= currentTimeMillis) {
                redisTemplate.delete(lockKey);
            } else {
                return false;
            }
        }
        Long expire = currentTimeMillis + expiresMilis;
        return Optional
                .ofNullable(redisTemplate.opsForValue().setIfAbsent(lockKey, expire))
                .orElse(false);

    }

    public void realeaseLock(String taskKey) {
        String lockKey = getLockKey(taskKey);
        redisTemplate.delete(lockKey);
    }

    public String getLockKey(String key) {
        return String.format(KEY_TEMPLATE, key);
    }

}
