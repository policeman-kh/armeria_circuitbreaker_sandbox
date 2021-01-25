package sandbox.armeria_webflux_breaker.test;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sandbox.armeria_webflux_breaker.breaker.CircuitBreakable;

@Service
public class TestService {
    @CircuitBreakable(name = "mono")
    public Mono<String> mono(String param) {
        return Mono.just(param);
    }

    @CircuitBreakable(name = "monoFailed")
    public Mono<String> monoFailed(String param) {
        return Mono.error(new IllegalStateException());
    }

    @CircuitBreakable(name = "flux")
    public Flux<String> flux(String param) {
        return Flux.just(param);
    }

    @CircuitBreakable(name = "fluxFailed")
    public Flux<String> fluxFailed(String param) {
        return Flux.error(new IllegalStateException());
    }
}
