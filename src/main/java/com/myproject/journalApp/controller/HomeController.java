package com.myproject.journalApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Home APIs",
        description = "Endpoints for home and health check operations."
)
public class HomeController {

    @GetMapping("/")
    @Operation(
            summary = "Health check endpoint",
            description = "Returns a simple response to verify the service is running."
    )
    @ApiResponse(responseCode = "200", description = "Service is up")
    public String healthCheck() {
        return "Home";
    }
}