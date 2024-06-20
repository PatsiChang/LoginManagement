import com.common.utils.ValidationHelper;
import com.patsi.Main;
import com.patsi.bean.Person;
import com.patsi.validator.EmailValidator;
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
public class EmailValidatorTest {

    @InjectMocks
    private EmailValidator emailValidator;


    final UUID validPersonId = UUID.randomUUID();
    final Person validPerson = new Person(validPersonId, "validUserID", "validUserName",
        "validEmail@gmail.com", "validPassword16To30Char##!");
    final Person invalidPerson = new Person(validPersonId, "validUserID", "validUserName",
        "invalidEmail", "validPassword16To30Char##!");
    final Map<String, Object> field = new HashMap<>();

    @Test
    void testEmailValidatorWithValidEmail() {
        assertEquals(null, emailValidator.validate(validPerson.getEmail(), field));
    }

    @Test
    void testEmailValidatorWithInvalidEmail() {
        assertEquals(List.of("Invalid Email!"), emailValidator.validate(invalidPerson.getEmail(), field));
    }

}
