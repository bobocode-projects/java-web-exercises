package com.bobocode;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.reflections.Reflections;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanPostProcessorTest {

    private Class<?> trimmedAnnotationClass;
    private Class<?> enableStringTrimmingAnnotationClass;

    public static final String ANNOTATIONS_PACKAGE = "com.bobocode.annotation";
    public static final String TRIMMED = "Trimmed";
    public static final String ENABLE_STRING_TRIMMING = "EnableStringTrimming";


    private Reflections annotationReflections;

    @BeforeEach
    @SneakyThrows
    public void init(){
        annotationReflections = new Reflections(ANNOTATIONS_PACKAGE);
        trimmedAnnotationClass = Class.forName(ANNOTATIONS_PACKAGE + "." + TRIMMED);
        enableStringTrimmingAnnotationClass = Class.forName(ANNOTATIONS_PACKAGE + "." + ENABLE_STRING_TRIMMING);
    }

    @Test
    @Order(1)
    @DisplayName("@Trimmed annotation exists")
    void trimmedAnnotationExists() throws ClassNotFoundException {
        Class.forName(ANNOTATIONS_PACKAGE + "." + TRIMMED);
    }

    @Test
    @Order(2)
    @DisplayName("Trimmed annotation class is marked by @Retention with \"Runtime\" policy")
    void trimmedAnnotationClassIsMarkedByRetentionRuntimePolicy(){
        Optional<Class<?>> annotationClass = getAnnotationClassMarketByRetentionRuntimePolicy(TRIMMED);

        assertThat(annotationClass).isPresent();
    }

    @Test
    @Order(3)
    @DisplayName("@EnableStringTrimming annotation exists")
    void enableStringTrimmingAnnotationExists() throws ClassNotFoundException {
        Class.forName(ANNOTATIONS_PACKAGE + "." + ENABLE_STRING_TRIMMING);
    }

    @Test
    @Order(4)
    @DisplayName("EnableStringTrimming annotation class is marked by @Retention with \"Runtime\" policy")
    void enableStringTrimmingAnnotationClassIsMarkedByRetentionRuntimePolicy(){
        Optional<Class<?>> annotationClass = getAnnotationClassMarketByRetentionRuntimePolicy(ENABLE_STRING_TRIMMING);

        assertThat(annotationClass).isPresent();
    }

    private Optional<Class<?>> getAnnotationClassMarketByRetentionRuntimePolicy(String className) {
        Set<Class<?>> annotations =
                annotationReflections.getTypesAnnotatedWith(Retention.class);
        return annotations.stream()
                .filter(c -> c.getSimpleName().equals(className))
                .filter(c -> c.getAnnotation(Retention.class).value().equals(RetentionPolicy.RUNTIME))
                .findAny();
    }
}
