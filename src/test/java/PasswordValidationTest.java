import com.common.utils.ValidationHelper;
import com.patsi.Main;
import com.patsi.bean.Person;
import com.patsi.validator.EmailValidator;
import com.patsi.validator.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
public class PasswordValidationTest {

    @InjectMocks
    private PasswordValidator passwordValidator;


    final UUID validPersonId = UUID.randomUUID();
    final Person validPerson = new Person(validPersonId, "validUserID", "validUserName",
        "validEmail@gmail.com", "validPassword16To30Char!");
    final Person invalidPersonWithTooShortPW = new Person(validPersonId, "validUserID", "validUserName",
        "validEmail@gmail.com", "tooShortPW!");
    final Person invalidPersonWithNoSpecialCharacter = new Person(validPersonId, "validUserID", "validUserName",
        "validEmail@gmail.com", "passwordNoSpecialChar");
    Map<String, Object> field = new HashMap<>();
    @BeforeEach
    void setUp(){
        field.put("min", Integer.valueOf(16));
        field.put("max", Integer.valueOf(30));
    }

    @Test
    void testPasswordValidatorWithValidPassword() {
        assertEquals(List.of(), passwordValidator.validate(validPerson.getPassword(), field));
    }

    @Test
    void testPasswordValidatorWithInvalidPassword() {
        assertEquals(List.of("Password Must be between 16 and 30 characters!")
            , passwordValidator.validate(invalidPersonWithTooShortPW.getPassword(), field));
    }

    @Test
    void testPasswordValidatorWithInvalidPassword2() {
        assertEquals(List.of("Password Must contains at least one special characters (ie. !*?$ )!")
            , passwordValidator.validate(invalidPersonWithNoSpecialCharacter.getPassword(), field));
    }

}
