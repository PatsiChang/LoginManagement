package validatorTests;

import com.patsi.Main;
import com.patsi.bean.Person;
import com.patsi.bean.UnverifiedPerson;
import com.patsi.repository.PersonRepository;
import com.patsi.repository.UnverifiedPersonRepository;
import com.patsi.validator.PersonRegistryValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@MockitoSettings(strictness = Strictness.LENIENT)
public class PersonRegistryValidationTest {
    @InjectMocks
    private PersonRegistryValidation personRegistryValidation;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UnverifiedPersonRepository unverifiedPersonRepository;

    static final String uniqueEmail = "uniqueEmail@gmail.com";
    static final String existedEmail = "existedEmail@gmail.com";
    static final String uniqueUserId = "uniqueUserId";
    static final String existedUserId = "existedUserId";

    UnverifiedPerson existingUnverifiedPerson = UnverifiedPerson.builder()
        .token("00000")
        .email(existedEmail)
        .name("existedEmail")
        .userId(existedUserId)
        .build();
    UnverifiedPerson newUnverifiedPerson = UnverifiedPerson.builder()
        .token("00000")
        .email(uniqueEmail)
        .name("existedEmail")
        .userId(uniqueUserId)
        .build();

    @Test
    void validateUnverifiedPersonTest() {
        when(personRepository.findByUserId(existedUserId))
            .thenReturn(Optional.ofNullable(new Person()));
        when(personRepository.findByEmail(existedEmail))
            .thenReturn(Optional.ofNullable(new Person()));
        when(unverifiedPersonRepository.findByUserId(existedUserId))
            .thenReturn(Optional.ofNullable(new UnverifiedPerson()));
        when(unverifiedPersonRepository.findByEmail(existedEmail))
            .thenReturn(Optional.ofNullable(new UnverifiedPerson()));

        assertEquals(List.of(), personRegistryValidation.validateUnverifiedPerson(newUnverifiedPerson));
        assertEquals(List.of("Email Already Exist!", "UserID Already Exist!"),
            personRegistryValidation.validateUnverifiedPerson(existingUnverifiedPerson));


    }


}
