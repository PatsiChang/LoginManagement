package com.patsi.serviceTests;

import com.patsi.bean.Person;
import com.patsi.bean.UnverifiedPerson;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommonTest {

    //Person Constants
    Person personA = Person.builder()
        .uid(UUID.fromString("012aa3f5-42bc-4834-bb54-2c6913f1832d"))
        .userId("personA")
        .name("personA")
        .email("personA@gmail.com")
        .password("personA")
        .build();
    Person personB = Person.builder()
        .uid(UUID.fromString("f45d3402-9a51-402e-ad2a-ff6de43f99d3"))
        .userId("personB")
        .name("personB")
        .email("personB@gmail.com")
        .password("personB")
        .build();

    List<Person> personList = List.of(personA, personB);

    //Unverified Person Constance
    protected UnverifiedPerson unverifiedPersonA = UnverifiedPerson.builder()
        .token("111111")
        .userId("unverifiedPersonA")
        .name("unverifiedPersonA")
        .email("unverifiedPersonA@gmail.com")
        .password("unverifiedPersonA")
        .build();

}
