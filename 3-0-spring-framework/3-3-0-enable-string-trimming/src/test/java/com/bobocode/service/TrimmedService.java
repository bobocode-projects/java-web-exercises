package com.bobocode.service;

import com.bobocode.Trimmed;
import org.springframework.stereotype.Service;

@Service
@Trimmed
public class TrimmedService {

    public String wrapStringWithSpaces(String string) {
        return "   " + string + "   ";
    }

    public String getTheSameString(String string) {
        return string;
    }
}
