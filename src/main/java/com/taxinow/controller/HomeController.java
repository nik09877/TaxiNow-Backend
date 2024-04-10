package com.taxinow.controller;

import com.taxinow.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("home")
    public ResponseEntity<MessageResponse> homeHandler() {
        MessageResponse msg = new MessageResponse("Welcome to TaxiNow application!");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
