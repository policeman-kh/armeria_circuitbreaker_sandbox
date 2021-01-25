package sandbox.armeria_webflux_breaker.breaker.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sandbox.armeria_webflux_breaker.breaker.CircuitBreakable;

public class Logic {
    @CircuitBreakable(name = "getMono")
    public Mono<String> getMono() {
        return Mono.just("");
    }

    @CircuitBreakable(name = "getMonoFailed")
    public Mono<String> getMonoFailed() {
        return Mono.error(new IllegalStateException());
    }

    @CircuitBreakable(name = "getFlux")
    public Flux<String> getFlux() {
        return Flux.just("");
    }

    @CircuitBreakable(name = "getFluxFailed")
    public Flux<String> getFluxFailed() {
        return Flux.error(new IllegalStateException());
    }

    @CircuitBreakable(name = "get")
    public String get() {
        return "";
    }

    @CircuitBreakable(name = "getFailed")
    public String getFailed() {
        throw new IllegalStateException();
    }
}
