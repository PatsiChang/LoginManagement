package com.patsi.service;

import com.common.email.utils.DateHelper;
import com.patsi.bean.Person;
import com.patsi.bean.UserLogin;
import com.patsi.repository.PersonRepository;
import com.patsi.repository.SessionRepository;
import com.patsi.utils.SHAHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class LoginService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LogInSessionService logInSessionService;
    @Autowired
    private LoginValueServices loginValueServices;

    Logger log = LoggerFactory.getLogger(LoginService.class);


    private boolean verifyPassword(Person p, String password) {
        byte[] passwordByte = password.getBytes(SHAHelper.UTF_8);
        byte[] saltBytes = loginValueServices.getSaltProperties().getBytes(StandardCharsets.UTF_8);
        byte[] passwordWithSalt = ByteBuffer.allocate(
                saltBytes.length + passwordByte.length)
            .put(saltBytes)
            .put(passwordByte)
            .array();
        byte[] hashedInputPassword = SHAHelper.digest(passwordWithSalt, loginValueServices.getAlgorithmProperties());
        return (p.getPassword().equals(SHAHelper.bytesToHex(hashedInputPassword)));
    }

    public String checkLogIn(UserLogin user) {
        if (logInSessionService.findPerson(user.getUserId()) != null) {
            Person p = personRepository.findByUserId(user.getUserId()).orElse(null);
            if (verifyPassword(p, user.getPassword())) {
                log.info("Successfully authenticated!");
                String tmpToken = logInSessionService.createUserToken();
                Long expiryTime = DateHelper.getCurrentDate().getTime() + 600000L;
                if (sessionRepository.findByCustomerId(p.getUid()).isPresent()) {
                    logInSessionService.endSession(p.getUid());
                    logInSessionService.createSession(p.getUid(), tmpToken, expiryTime);
                    return tmpToken;
                } else {
                    logInSessionService.createSession(p.getUid(), tmpToken, expiryTime);
                    return tmpToken;
                }
            }
        }
        return null;
    }

    public boolean logOut(String token) {
        log.info("In logOut");
        logInSessionService.endSessionByToken(token);
        return true;
    }


}
