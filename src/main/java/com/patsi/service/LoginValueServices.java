package com.patsi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginValueServices {

    @Value("${saltForPasswordEncryption}")
    String salt;
    @Value("${algorithmForPasswordEncryption}")
    String algorithm;

    public String getSaltProperties () {
        return salt;
    }
    public String getAlgorithmProperties () {
        return algorithm;
    }
}
