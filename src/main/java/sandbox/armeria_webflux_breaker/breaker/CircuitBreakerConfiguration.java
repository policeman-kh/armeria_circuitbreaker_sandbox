package sandbox.armeria_webflux_breaker.breaker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.linecorp.armeria.client.circuitbreaker.MetricCollectingCircuitBreakerListener;

import io.micrometer.core.instrument.MeterRegistry;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class CircuitBreakerConfiguration {
    @Bean
    public MetricCollectingCircuitBreakerListener circuitBreakerListener(MeterRegistry meterRegistry) {
        return new MetricCollectingCircuitBreakerListener(meterRegistry);
    }
}
