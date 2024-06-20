package com.patsi.controller;

import com.patsi.bean.Person;
import com.patsi.service.PersonInfoService;
import com.patsi.service.ValidatorService;
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

    @PostMapping
    public List<String> registerPerson(@RequestBody @Valid Person person) throws MessagingException {
        List<String> errList = validatorService.callValidator(person);
        if (errList != null) {
            return errList;
        } else {
            personInfoService.registerPerson(person);
            return List.of();
        }
    }

    @GetMapping
    public boolean getPerson(@RequestParam String userId) {
        return personInfoService.getPerson(userId);
    }

    @GetMapping("/getAllPerson")
    public List<Person> getAllPerson() {
        return personInfoService.getAllPerson();
    }

    @DeleteMapping
    public void deletePerson(@RequestBody Person person) {
        System.out.println("controller" + person.getUid());
        personInfoService.deletePeron(person);
    }
}
