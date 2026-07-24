package com.disastermanagement.controller;

import com.disastermanagement.dto.InfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/info")
    public InfoResponse getInfo() {
        return new InfoResponse(
                "Disaster Management System API",
                "UP",
                "1.0.0"
        );
    }
}
