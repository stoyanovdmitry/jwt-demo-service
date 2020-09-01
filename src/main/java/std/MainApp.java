package std;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import std.handler.CustomRestTemplateErrorHandler;

@SpringBootApplication
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Bean
    public RestTemplate restTemplate(CustomRestTemplateErrorHandler errorHandler) {
        return new RestTemplateBuilder().errorHandler(errorHandler)
                                        .build();
    }
}
