package com.patsi.serviceTests;

import com.patsi.Main;
import com.patsi.service.LoginValueServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

//@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
public class LoginValueServiceTest {
    @Value("${saltForPasswordEncryption}")
    String salt;
    @Value("${algorithmForPasswordEncryption}")
    String algorithm;

    @Autowired
    private LoginValueServices loginValueServices;

    @Test
    void testGetEnableEmailFlagWithTrueValue() {
        String result = loginValueServices.getSaltProperties();
        assertEquals(result, "8ab15c1c1c271e297c3f6d34e695b3f8");
    }
    @Test
    void testGetAlgorithmProperties() {
        String result = loginValueServices.getAlgorithmProperties();
        assertEquals(result, "SHA3-256");
    }
}
