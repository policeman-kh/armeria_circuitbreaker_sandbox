package sandbox.armeria_webflux_breaker.breaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CircuitBreakable {
    String name();

    CircuitBreakerGroup group() default CircuitBreakerGroup.DEFAULT;
}
