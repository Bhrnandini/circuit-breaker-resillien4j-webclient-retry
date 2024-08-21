package com.demo.circuitbreaker.controller;

import com.demo.circuitbreaker.configuration.CustomException;
import com.demo.circuitbreaker.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/circuit")
public class MyController {

    @Autowired
    MyService myService;

    @GetMapping("/getResponse")
    public Mono<String> getResponse() throws CustomException {
        System.out.println("inside controller");
        return myService.getResponse();
    }
}