package sandbox.armeria_webflux_breaker.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class TestController {
    private final TestService testService;

    @GetMapping("/get")
    public Mono<String> getMono(String param) {
        return testService.mono(param);
    }

    @GetMapping("/getFailed")
    public Mono<String> getMonoFailed(String param) {
        return testService.monoFailed(param);
    }

    @GetMapping("/flux")
    public Flux<String> getFlux(String param) {
        return testService.flux(param);
    }

    @GetMapping("/fluxFailed")
    public Flux<String> getFluxFailed(String param) {
        return testService.fluxFailed(param);
    }
}
