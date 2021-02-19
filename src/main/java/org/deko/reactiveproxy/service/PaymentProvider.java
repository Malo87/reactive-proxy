package org.deko.reactiveproxy.service;

import org.deko.reactiveproxy.model.Payment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Component
public class PaymentProvider {

    public static final String PSP_URL = "http://localhost:9090/";

    private final WebClient webClient;

    public PaymentProvider() {
        WebClient client = WebClient.builder()
                .baseUrl(PSP_URL)
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient = client;
    }

    public Mono<Payment> getPayment(UUID id) {
        final WebClient.RequestHeadersUriSpec<?> uriSpec = webClient.get();
        uriSpec.uri("payment/"+id.toString());
        return uriSpec.exchangeToMono(response -> {
            if (response.statusCode()
                    .equals(OK)) {
                return response.bodyToMono(Payment.class);
            } else {
                return response.createException()
                        .flatMap(Mono::error);
            }
        });
    }
}
