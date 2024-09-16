package com.patsi.service;

import com.common.email.utils.DateHelper;
import com.patsi.bean.Person;
import com.patsi.bean.UserLogin;
import com.patsi.repository.PersonRepository;
import com.patsi.repository.SessionRepository;
import com.patsi.utils.SHAHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LogInSessionService logInSessionService;
    @Autowired
    private LoginValueServices loginValueServices;

    private boolean verifyPassword(Person person, String password) {
        byte[] passwordByte = password.getBytes(SHAHelper.UTF_8);
        byte[] saltBytes = loginValueServices.getSaltProperties().getBytes(StandardCharsets.UTF_8);
        byte[] passwordWithSalt = ByteBuffer.allocate(
                saltBytes.length + passwordByte.length)
            .put(saltBytes)
            .put(passwordByte)
            .array();
        byte[] hashedInputPassword = SHAHelper.digest(passwordWithSalt, loginValueServices.getAlgorithmProperties());
        return (person.getPassword().equals(SHAHelper.bytesToHex(hashedInputPassword)));
    }

    public String checkLogIn(UserLogin user) {
        Person person = personRepository.findByUserId(user.getUserId()).orElse(null);
        if (person != null) {
            if (verifyPassword(person, user.getPassword())) {
                log.info("Successfully authenticated!");
                String tmpToken = logInSessionService.createUserToken();
                Long expiryTime = DateHelper.getCurrentDate().getTime() + 600000L;
                UUID uid = person.getUid();
                return sessionRepository.findByCustomerId(uid)
                    .map(logInSession -> {
                        logInSessionService.endSession(uid);
                        logInSessionService.createSession(person.getUid(), tmpToken, expiryTime);
                        return tmpToken;
                    }).orElseGet(() -> {
                        logInSessionService.createSession(person.getUid(), tmpToken, expiryTime);
                        return tmpToken;
                    });
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
