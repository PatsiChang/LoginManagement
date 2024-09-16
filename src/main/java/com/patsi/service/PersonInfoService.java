package com.patsi.service;

import com.common.email.bean.Email;
import com.common.email.service.EmailService;
import com.patsi.bean.Person;
import com.patsi.bean.UnverifiedPerson;
import com.patsi.repository.PersonRepository;
import com.patsi.repository.UnverifiedPersonRepository;
import com.patsi.utils.SHAHelper;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
public class PersonInfoService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UnverifiedPersonRepository unverifiedPersonRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LoginValueServices loginValueServices;
    @Autowired
    private FlagToggleServices flagToggleServices;


    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

    //Encrypt Password before store to DB
    private String passwordEncryption(String password) {
        log.info("In Password Encryption");
        byte[] passwordByte = password.getBytes(SHAHelper.UTF_8);
        byte[] saltBytes = loginValueServices.getSaltProperties().getBytes(StandardCharsets.UTF_8);
        byte[] passwordWithSalt = ByteBuffer.allocate(
                saltBytes.length + passwordByte.length)
            .put(saltBytes)
            .put(passwordByte)
            .array();
        byte[] dm = SHAHelper.digest(passwordWithSalt, loginValueServices.getAlgorithmProperties());
        return SHAHelper.bytesToHex(dm);
    }

    @Transactional
    public String registerVerifiedPerson(String token, String email) {
        if (token.length() == 6 && personRepository.findByEmail(email).isPresent()) {
            return "Account already Activated";
        }
        return unverifiedPersonRepository.findByToken(token)
            .map(unverifiedPerson -> {
                if (unverifiedPerson.getEmail().equals(email)) {
                    String encryptedPassword = passwordEncryption(unverifiedPerson.getPassword());
                    personRepository.save(
                        Person.builder()
                            .userId(unverifiedPerson.getUserId())
                            .email(unverifiedPerson.getEmail())
                            .name(unverifiedPerson.getName())
                            .password(encryptedPassword)
                            .build()
                    );
                    unverifiedPersonRepository.deleteByToken(token);
                    return "Successfully Created Account";
                }
                return "Passcode mismatch Email!";
            })
            .orElse("Invalid Token!");

    }

    //Todo: Set Expiry for staged profile?
    public String registerUnverifiedPerson(UnverifiedPerson unverifiedPerson) throws MessagingException {
        String emailVerifyToken = String.valueOf(new Random().nextInt(900000) + 100000);
        unverifiedPerson.setToken(emailVerifyToken);
        unverifiedPersonRepository.save(unverifiedPerson);
        if (flagToggleServices.getEnableEmailFlag()) {
            sendVerifyEmail(unverifiedPerson);
        }
        return "";

    }

    private boolean sendVerifyEmail(UnverifiedPerson unverifiedPerson) throws MessagingException {
        Email sighUpEmail = new Email(unverifiedPerson.getEmail(), unverifiedPerson.getName(), unverifiedPerson.getToken(),
            "Sign Up Notification", "Welcome to Smart Home", true);
        emailService.sendEmail(sighUpEmail);
        return true;
    }

    public void deletePeron(Person person) {
        personRepository.deleteById(person.getUid());
    }
}
