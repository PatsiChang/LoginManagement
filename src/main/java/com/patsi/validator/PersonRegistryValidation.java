package com.patsi.validator;

import com.common.commonUtils.ListHelper;
import com.patsi.bean.UnverifiedPerson;
import com.patsi.repository.PersonRepository;
import com.patsi.repository.UnverifiedPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PersonRegistryValidation {
    @Autowired
    private UnverifiedPersonRepository unverifiedPersonRepository;
    @Autowired
    private PersonRepository personRepository;

    //Return true if email does not exist in any repo
    private boolean validatePersonEmailSingularity(String email) {
        return !unverifiedPersonRepository.findByEmail(email).isPresent()
            && !personRepository.findByEmail(email).isPresent();
    }
    //Return true if userId does not exist in any repo
    private boolean validatePersonUserIdSingularity(String userId) {
        return !unverifiedPersonRepository.findByUserId(userId).isPresent()
            && !personRepository.findByUserId(userId).isPresent();
    }

    public List<String> validateUnverifiedPerson(UnverifiedPerson unverifiedPerson) {
        List<String> listOfValidatedResult = ListHelper.newList();
        if (!validatePersonEmailSingularity(unverifiedPerson.getEmail())){
            listOfValidatedResult.add("Email Already Exist!");
        }
        if(!validatePersonUserIdSingularity(unverifiedPerson.getUserId())){
            listOfValidatedResult.add("UserID Already Exist!");
        }
        return listOfValidatedResult;
    }

}
