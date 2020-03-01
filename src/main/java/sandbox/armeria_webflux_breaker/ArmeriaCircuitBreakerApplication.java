package sandbox.armeria_webflux_breaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArmeriaCircuitBreakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArmeriaCircuitBreakerApplication.class, args);
    }

}
