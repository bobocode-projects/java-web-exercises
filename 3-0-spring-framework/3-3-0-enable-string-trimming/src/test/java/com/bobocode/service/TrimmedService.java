package com.bobocode.service;

import com.bobocode.annotation.Trimmed;
import org.springframework.stereotype.Service;

@Service
public class TrimmedService {

    public String getTheTrimmedString(@Trimmed String string) {
        return string;
    }

    public String getNotTrimmedString(String string) {
        return string;
    }
}
