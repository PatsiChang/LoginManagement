package com.patsi.controller;

import com.common.validation.service.ValidatorService;
import com.patsi.bean.Person;
import com.patsi.bean.UnverifiedPerson;
import com.patsi.service.PersonInfoService;
import com.patsi.validator.PersonRegistryValidation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/PersonInfo")
@CrossOrigin
@Validated
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ValidatorService validatorService;
    @Autowired
    private PersonRegistryValidation personRegistryValidation;

    @PostMapping
    public List<String> registerUnverifiedPerson(@RequestBody @Valid UnverifiedPerson unverifiedPerson)
        throws MessagingException {
        List<String> errList = validatorService.checkAnnotation(unverifiedPerson);
        if (!errList.isEmpty()) {
            return errList;
        } else {
            List<String> validationResult = personRegistryValidation.validateUnverifiedPerson(unverifiedPerson);
            if (validationResult.isEmpty()) {
                personInfoService.registerUnverifiedPerson(unverifiedPerson);
            }
            return validationResult;
        }
    }
    @PostMapping("/verifyEmail")
    public String emailVerificationResponse(@RequestParam String emailVerifyToken, String email) {
        return personInfoService.registerVerifiedPerson(emailVerifyToken, email);
    }

//    @GetMapping
//    public boolean getPerson(@RequestParam String userId) {
//        return personInfoService.(userId);
//    }

    @GetMapping("/getAllPerson")
    public List<Person> getAllPerson() {
        return personInfoService.getAllPerson();
    }

    @DeleteMapping
    public void deletePerson(@RequestBody Person person) {
        personInfoService.deletePeron(person);
    }
}
