package sandbox.armeria_webflux_breaker.test;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sandbox.armeria_webflux_breaker.breaker.CircuitBreaker;

@Service
public class TestService {
    @CircuitBreaker(name = "mono")
    public Mono<String> mono(String param) {
        return Mono.just(param);
    }
    @CircuitBreaker(name = "monoFailed")
    public Mono<String> monoFailed(String param) {
        return Mono.error(new IllegalStateException());
    }

    @CircuitBreaker(name = "flux")
    public Flux<String> flux(String param) {
        return Flux.just(param);
    }

    @CircuitBreaker(name = "fluxFailed")
    public Flux<String> fluxFailed(String param) {
        return Flux.error(new IllegalStateException());
    }
}
