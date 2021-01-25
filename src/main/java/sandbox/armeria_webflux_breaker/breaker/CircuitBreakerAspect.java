package sandbox.armeria_webflux_breaker.breaker;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;
import com.linecorp.armeria.client.circuitbreaker.FailFastException;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@AllArgsConstructor
@Component
public class CircuitBreakerAspect {
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Around("@annotation(sandbox.armeria_webflux_breaker.breaker.CircuitBreakable)")
    public Object circuitBreakerAround(ProceedingJoinPoint joinPoint)
            throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final CircuitBreakable annotation =
                method.getAnnotation(CircuitBreakable.class);
        final CircuitBreaker cb = circuitBreakerRegistry.getCircuitBreaker(annotation.name(),
                                                                           annotation.group());
        final Class<?> returnType = method.getReturnType();
        if (Mono.class.isAssignableFrom(returnType)) {
            if (!cb.canRequest()) {
                return Mono.error(new FailFastException(cb));
            }
            final Mono<?> mono = (Mono<?>) joinPoint.proceed();
            mono.subscribe(new CircuitBreakerObserver<>(cb, annotation.group()));
            return mono;
        } else if (Flux.class.isAssignableFrom(returnType)) {
            if (!cb.canRequest()) {
                return Flux.error(new FailFastException(cb));
            }
            final Flux<?> flux = (Flux<?>) joinPoint.proceed();
            flux.subscribe(new CircuitBreakerObserver<>(cb, annotation.group()));
            return flux;
        } else {
            if (!cb.canRequest()) {
                throw new FailFastException(cb);
            }
            boolean success = true;
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                success = !annotation.group().getExceptionFilter().shouldDealWith(e);
                throw e;
            } finally {
                if (success) {
                    cb.onSuccess();
                } else {
                    cb.onFailure();
                }
            }
        }
    }
}
