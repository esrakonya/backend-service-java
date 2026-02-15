package org.esrakonya.backend.gateway.controller;

import org.esrakonya.backend.common.core.dto.MessageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/products")
    public MessageResponse productFallback() {
        return new MessageResponse("Product Service is temporarily unavailable. This is a graceful fallback.");
    }
}
