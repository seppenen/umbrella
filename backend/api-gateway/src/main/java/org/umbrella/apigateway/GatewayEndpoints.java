package org.umbrella.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayEndpoints {

    @GetMapping("/")
    public String getHello() {
        return "Hello from API Gateway";
    }
}
