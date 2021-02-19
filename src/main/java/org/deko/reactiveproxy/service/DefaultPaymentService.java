package org.deko.reactiveproxy.service;

import org.deko.reactiveproxy.model.Payment;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Component
public class DefaultPaymentService implements PaymentService {

    public static final Duration CACHE_TTL = Duration.ofMinutes(2);

    private final ReactiveRedisOperations<String, Payment> paymentOperations;
    private final ReactiveValueOperations<String, Payment> valueOperations;

    private final PaymentProvider paymentProvider;

    public DefaultPaymentService(ReactiveRedisOperations<String, Payment> paymentTemplate,PaymentProvider provider) {
        this.paymentProvider=provider;
        this.paymentOperations=paymentTemplate;
        this.valueOperations =paymentOperations.opsForValue();
    }

    @Override public Mono<Payment> getPayment(UUID id) {
        return valueOperations.get(id.toString()).switchIfEmpty(paymentProvider.getPayment(id));
    }

    @Override public Flux<Payment> getAllPayments() {
        return paymentOperations.keys("*")
                .flatMap(valueOperations::get);
    }

    @Override public Mono<Payment> addToCache(Payment payment) {
        final var key = payment.getId().toString();
        valueOperations.set(key,payment, CACHE_TTL);
        return Mono.just(payment);
    }
}
