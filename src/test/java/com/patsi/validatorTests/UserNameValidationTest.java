package com.patsi.validatorTests;

import com.common.validation.bean.ProfanityWords;
import com.common.validation.repository.ProfanityWordsRepository;
import com.patsi.Main;
import com.patsi.bean.Person;
import com.patsi.validator.UserNameValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Main.class)
public class UserNameValidationTest {

    @InjectMocks
    private UserNameValidation userNameValidation;

    @Mock
    private ProfanityWordsRepository profanityWordsRepository;

    final UUID validPersonId = UUID.randomUUID();
    final Person validPerson = new Person(validPersonId, "validUserID", "validUserName",
        "validEmail@gmail.com", "validPassword16To30Char##!");
    final Person invalidPerson = new Person(validPersonId, "validUserID", "BadWord test",
        "validEmail@gmail.com", "validPassword16To30Char##!");
    final Map<String, Object> field = new HashMap<>();
    final List<ProfanityWords> profanityWordList = List.of(new ProfanityWords("BadWord"));

    @BeforeEach
    void setup() {
        when(profanityWordsRepository.findAll())
            .thenReturn(profanityWordList);
    }

    @Test
    void testUserNameValidatorWithValidUsername() {
        assertEquals(List.of(), userNameValidation.validate(validPerson.getName(), field));
    }

    @Test
    void testUserNameValidatorWithInvalidUsername() {
        assertEquals(List.of("UserName Must not contain Profanity words!")
            , userNameValidation.validate(invalidPerson.getName(), field));
    }

}
