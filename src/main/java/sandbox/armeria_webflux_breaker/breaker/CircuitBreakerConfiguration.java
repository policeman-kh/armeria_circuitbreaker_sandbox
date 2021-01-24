package sandbox.armeria_webflux_breaker.breaker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerListener;

import io.micrometer.core.instrument.MeterRegistry;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class CircuitBreakerConfiguration {
    @Bean
    public CircuitBreakerListener circuitBreakerListener(MeterRegistry meterRegistry) {
        return CircuitBreakerListener.metricCollecting(meterRegistry);
    }
}
