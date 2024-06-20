package com.patsi.validator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;


public interface Validator {

    Class accept();

    List<String> validate(Object input, Map<String, Object> field);
}
