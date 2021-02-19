package org.deko.reactiveproxy;

import org.deko.reactiveproxy.model.Payment;
import org.deko.reactiveproxy.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment/{id}")
    public Mono<Payment> getReactivePayment(@PathVariable UUID id) {
        return paymentService.getPayment(id).flatMap(paymentService::addToCache);
    }
}
