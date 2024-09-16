package com.patsi.controller;


import com.patsi.bean.UserLogin;
import com.patsi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public String checkLogIn(@RequestBody UserLogin userLogin) {
        return loginService.checkLogIn(userLogin);
    }

    @PostMapping("/logout")
    public boolean logOut(String token) {
        loginService.logOut(token);
        return true;
    }

}
