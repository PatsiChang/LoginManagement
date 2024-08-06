package controllerTests;


import com.common.validation.service.ValidatorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.patsi.Main;
import com.patsi.service.PersonInfoService;
import com.patsi.validator.PersonRegistryValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import serviceTests.CommonTest;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class PersonInfoControllerTest extends CommonTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PersonInfoService personInfoService;
    @MockBean
    private ValidatorService validatorService;
    @MockBean
    private PersonRegistryValidation personRegistryValidation;


    @Test
    void registerUnverifiedPersonTestWithValidData() throws Exception {
        when(validatorService.checkAnnotation(unverifiedPersonA))
            .thenReturn(List.of());
        when(personRegistryValidation.validateUnverifiedPerson(unverifiedPersonA))
            .thenReturn(List.of());

        mockMvc.perform(post("/PersonInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unverifiedPersonA)))
            .andExpectAll(
                status().isOk(),
                jsonPath("$", hasSize(0))
            );
    }
    @Test
    void registerUnverifiedPersonTestWithInvalidData() throws Exception {
        when(validatorService.checkAnnotation(unverifiedPersonA))
            .thenReturn(List.of("Wrong Data"));
        when(personRegistryValidation.validateUnverifiedPerson(unverifiedPersonA))
            .thenReturn(List.of());

        mockMvc.perform(post("/PersonInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unverifiedPersonA)))
            .andExpectAll(
                status().isOk(),
                jsonPath("$[0]").value("Wrong Data")
            );
    }

    @Test
    void emailVerificationResponseTest() throws Exception {
        when(personInfoService.registerVerifiedPerson("123456", "unverifiedPersonA@gmail.com"))
            .thenReturn("success");
        when(personInfoService.registerVerifiedPerson("123333", "unverifiedPersonA@gmail.com"))
            .thenReturn("unsuccessful");


        mockMvc.perform(post("/PersonInfo/verifyEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("emailVerifyToken", "123456")
                .param("email", "unverifiedPersonA@gmail.com"))
            .andExpectAll(
                status().isOk(),
                content().string("success")
            );

        mockMvc.perform(post("/PersonInfo/verifyEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("emailVerifyToken", "123333")
                .param("email", "unverifiedPersonA@gmail.com"))
            .andExpectAll(
                status().isOk(),
                content().string("unsuccessful")
            );
    }

    @Test
    void getAllPersonTest() throws Exception {
        when(personInfoService.getAllPerson())
            .thenReturn(List.of());

        mockMvc.perform(get("/PersonInfo/getAllPerson"))
            .andExpectAll(
                status().isOk(),
                jsonPath("$", hasSize(0))
            );

    }

    @Test
    void deletePersonTest() throws Exception {
        mockMvc.perform(delete("/PersonInfo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(unverifiedPersonA)))
            .andExpectAll(
                status().isOk()
            );

    }


}
