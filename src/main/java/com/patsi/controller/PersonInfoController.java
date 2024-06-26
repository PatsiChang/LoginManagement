package com.patsi.controller;

import com.patsi.bean.Person;
import com.patsi.service.PersonInfoService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/PersonInfo")
@CrossOrigin
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;

    @PostMapping
    public boolean registerPerson(@RequestBody Person person) throws MessagingException {
        return personInfoService.registerPerson(person);
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
