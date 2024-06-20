package com.patsi.validator;

import com.common.utils.ValidationHelper;
import com.patsi.annotations.IsEmail;
import com.patsi.utils.ListHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class EmailValidator implements Validator {
    public Class accept() {
        return IsEmail.class;
    }

    public List<String> validate(Object input, Map<String, Object> field){
        log.info("In EmailValidator");
        List<String> errorList = ListHelper.newList();
            if (!ValidationHelper.validateValidEmail(input.toString())) {
                errorList.add("Invalid Email!");
                return errorList;
            }
        return null;
    };

}
