package org.deko.reactiveproxy.service;

import org.deko.reactiveproxy.model.Payment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
  Mono<Payment> getPayment(UUID id);
  Flux<Payment> getAllPayments();

  Mono<Payment> addToCache(Payment payment);
}
