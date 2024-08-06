package com.patsi.controller;

import com.patsi.bean.Person;
import com.patsi.service.LogInSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/logInSession")
@CrossOrigin

public class LogInSessionController {
    @Autowired
    private LogInSessionService logInSessionService;

    @GetMapping
    public Person findPersonByToken(@RequestHeader(AUTHORIZATION) String token) {
        return logInSessionService.findPersonByToken(token);
    }

    @GetMapping("/getPersonUid")
    public ResponseEntity<String> findPersonUidByToken(@RequestHeader(AUTHORIZATION) String token) {
        Person p = logInSessionService.findPersonByToken(token);
        return (p != null) ? new ResponseEntity<>(p.getUid().toString(), HttpStatus.OK)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    @PutMapping
    public boolean renewToken(String token) {
        return logInSessionService.renewSession(token);
    }
}
