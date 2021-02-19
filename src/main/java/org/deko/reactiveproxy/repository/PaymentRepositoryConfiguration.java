package org.deko.reactiveproxy.repository;

import org.deko.reactiveproxy.model.Payment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class PaymentRepositoryConfiguration {
    @Bean
    ReactiveRedisOperations<String, Payment> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Payment> serializer = new Jackson2JsonRedisSerializer<>(Payment.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Payment> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Payment> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}