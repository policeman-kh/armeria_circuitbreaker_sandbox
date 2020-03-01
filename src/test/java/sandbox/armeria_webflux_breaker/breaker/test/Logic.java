package sandbox.armeria_webflux_breaker.breaker.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sandbox.armeria_webflux_breaker.breaker.CircuitBreaker;

public class Logic {
    @CircuitBreaker(name = "getMono")
    public Mono<String> getMono() {
        return Mono.just("");
    }

    @CircuitBreaker(name = "getMonoFailed")
    public Mono<String> getMonoFailed() {
        return Mono.error(new IllegalStateException());
    }

    @CircuitBreaker(name = "getFlux")
    public Flux<String> getFlux() {
        return Flux.just("");
    }

    @CircuitBreaker(name = "getFluxFailed")
    public Flux<String> getFluxFailed() {
        return Flux.error(new IllegalStateException());
    }

    @CircuitBreaker(name = "get")
    public String get() {
        return "";
    }

    @CircuitBreaker(name = "getFailed")
    public String getFailed() {
        throw new IllegalStateException();
    }
}
