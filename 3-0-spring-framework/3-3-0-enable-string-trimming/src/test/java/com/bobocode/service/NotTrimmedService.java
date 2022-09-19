package com.bobocode.service;

import org.springframework.stereotype.Service;

@Service
public class NotTrimmedService {

    public String wrapStringWithSpaces(String string) {
        return "   " + string + "    ";
    }

    public String getTheSameString(String string) {
        return string;
    }
}
