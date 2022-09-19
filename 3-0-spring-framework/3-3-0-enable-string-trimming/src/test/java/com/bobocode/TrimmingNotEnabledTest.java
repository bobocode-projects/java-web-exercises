package com.bobocode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bobocode.config.TrimmingNotEnabledConfig;
import com.bobocode.service.TrimmedService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = TrimmingNotEnabledConfig.class)
class TrimmingNotEnabledTest {

    @Test
    @DisplayName("TrimmedAnnotationBeanPostProcessor does not trims String input parameters of methods from the class marked by " +
            "@Trimmed if trimming is not enabled by @EnableStringTrimming")
    void trimmedAnnotationPostProcessor_trimmedClass_inputParams(@Autowired TrimmedService service) {
        String inputArgs = "   Mufasa LionKing  ";
        String actual = service.getTheSameString(inputArgs);

        assertEquals(inputArgs, actual);
    }

    @Test
    @DisplayName("TrimmedAnnotationBeanPostProcessor does not trims String return values of methods from class marked by " +
            "@Trimmed if trimming is not enabled by @EnableStringTrimming ")
    void trimmedAnnotationPostProcessor_trimmedClass_returnValue(@Autowired TrimmedService service) {
        String inputArgs = "Mufasa LionKing";
        String actual = service.wrapStringWithSpaces(inputArgs);

        TrimmedService outOfContestService = new TrimmedService();

        assertEquals(outOfContestService.wrapStringWithSpaces(inputArgs), actual);
    }
}
