package sandbox.armeria_webflux_breaker.breaker;

@FunctionalInterface
public interface ExceptionFilter {
    public boolean shouldDealWith(Throwable throwable);
}
