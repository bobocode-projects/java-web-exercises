package com.bobocode.service;

import com.bobocode.annotation.Trimmed;
import org.springframework.stereotype.Service;

@Service
public class TrimmedService {

    public String getTheTrimmedString(@Trimmed String string) {
        return string;
    }

    public String getTheTrimmedInteger(@Trimmed Integer num) {
        return num.toString();
    }

    public String getTheTrimmedStringWithTwoArgs(@Trimmed String stringOne, String stringTwo) {
        return stringOne.concat(stringTwo);
    }

    public String getTheTrimmedStringWithThreeArgs(@Trimmed String stringOne, String stringTwo, @Trimmed String stringThree) {
        return stringOne.concat(stringTwo).concat(stringThree);
    }

    public String getNotTrimmedString(String string) {
        return string;
    }
}
