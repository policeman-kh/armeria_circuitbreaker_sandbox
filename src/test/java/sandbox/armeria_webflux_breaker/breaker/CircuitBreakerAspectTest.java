package sandbox.armeria_webflux_breaker.breaker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerListener;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import sandbox.armeria_webflux_breaker.breaker.test.Logic;

public class CircuitBreakerAspectTest {
    private final CircuitBreakerListener circuitBreakerListener =
            CircuitBreakerListener.metricCollecting(new SimpleMeterRegistry());

    @Test
    public void testMonoSuccess() throws Throwable {
        final CircuitBreakerRegistry registry =
                new CircuitBreakerRegistry(circuitBreakerListener);
        final Logic logic = getLogic(registry);
        for (int i = 0; i < 100; i++) {
            logic.getMono().ignoreElement().block();
        }

        final CircuitBreaker cb = registry.getCircuitBreaker("getMono");
        assertThat(cb.canRequest()).isTrue();
    }

    @Test
    public void testMonoFailure() throws Throwable {
        final CircuitBreakerRegistry circuitBreakerRegistry =
                new CircuitBreakerRegistry(circuitBreakerListener);
        final Logic logic = getLogic(circuitBreakerRegistry);
        for (int i = 0; i < 100; i++) {
            logic.getMonoFailed().onErrorReturn("").ignoreElement().block();
            Thread.sleep(10);
        }
        final CircuitBreaker cb = circuitBreakerRegistry.getCircuitBreaker("getMonoFailed");
        assertThat(cb.canRequest()).isFalse();
    }

    @Test
    public void testFluxSuccess() throws Throwable {
        final CircuitBreakerRegistry circuitBreakerRegistry =
                new CircuitBreakerRegistry(circuitBreakerListener);
        final Logic logic = getLogic(circuitBreakerRegistry);
        for (int i = 0; i < 100; i++) {
            logic.getFlux().single().ignoreElement().block();
        }

        final CircuitBreaker cb = circuitBreakerRegistry.getCircuitBreaker("getFlux");
        assertThat(cb.canRequest()).isTrue();
    }

    @Test
    public void testFluxFailure() throws Throwable {
        final CircuitBreakerRegistry circuitBreakerRegistry =
                new CircuitBreakerRegistry(circuitBreakerListener);
        final Logic logic = getLogic(circuitBreakerRegistry);
        for (int i = 0; i < 100; i++) {
            logic.getFluxFailed().single().onErrorReturn("").ignoreElement().block();
            Thread.sleep(10);
        }
        final CircuitBreaker cb = circuitBreakerRegistry.getCircuitBreaker("getFluxFailed");
        assertThat(cb.canRequest()).isFalse();
    }

    @Test
    public void testSuccess() throws Throwable {
        final CircuitBreakerRegistry circuitBreakerRegistry =
                new CircuitBreakerRegistry(circuitBreakerListener);
        final Logic logic = getLogic(circuitBreakerRegistry);
        for (int i = 0; i < 100; i++) {
            logic.get();
        }

        final CircuitBreaker cb = circuitBreakerRegistry.getCircuitBreaker("get");
        assertThat(cb.canRequest()).isTrue();
    }

    @Test
    public void testFailure() throws Throwable {
        final CircuitBreakerRegistry circuitBreakerRegistry =
                new CircuitBreakerRegistry(circuitBreakerListener);
        final Logic logic = getLogic(circuitBreakerRegistry);
        for (int i = 0; i < 100; i++) {
            try {
                logic.getFailed();
            } catch (Throwable e) {}
            Thread.sleep(10);
        }
        final CircuitBreaker cb = circuitBreakerRegistry.getCircuitBreaker("getFailed");
        assertThat(cb.canRequest()).isFalse();
    }

    private static Logic getLogic(CircuitBreakerRegistry circuitBreakerRegistry) {
        final AspectJProxyFactory factory = new AspectJProxyFactory(new Logic());
        factory.addAspect(new CircuitBreakerAspect(circuitBreakerRegistry));
        return factory.getProxy();
    }
}
