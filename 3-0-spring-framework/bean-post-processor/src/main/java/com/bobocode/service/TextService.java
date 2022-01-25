package com.bobocode.service;

import com.bobocode.annotation.Trimmed;
import org.springframework.stereotype.Service;

/**
 * Configure the service to provide trimming process.
 */

@Service
@Trimmed
public class TextService {
    public String savedText;
    private final static String AVAILABLE_TEXT = "    Who cares about tabbing?    ";

    public void saveText(String text){
        savedText = text;
    }

    public String getAvailableText(){
        return AVAILABLE_TEXT;
    }
}
