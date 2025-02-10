package org.online.queue.backend_java.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.ttl}")
    private Long ttl;

    @Bean
    public RedisCacheManager cacheManager() {
        var configuration = new RedisStandaloneConfiguration(host, port);
        var connectionFactory = new JedisConnectionFactory(configuration);

        connectionFactory.start();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(ttl))
                        .disableCachingNullValues())
                .build();
    }
}
