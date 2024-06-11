package bet.pobrissimo.infra.seeders.redis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RedisTestRunner implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTestRunner(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
        redisTemplate.expireAt("testKey", Instant.now().plusSeconds(1200));
    }
}
