package com.bobocode;

import com.bobocode.config.ApplicationConfig;
import com.bobocode.service.TextService;
import com.bobocode.testService.NotTrimmedTextService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(BeanPostProcessorTest.TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanPostProcessorTest {
    @Configuration
    @ComponentScan(basePackages = "com.bobocode")
    static class TestConfig {
    }

    @Autowired
    TextService textService;

    @Autowired
    NotTrimmedTextService notTrimmedTextService;

    private static final String ANNOTATIONS_PACKAGE = "com.bobocode.annotation";
    private static final String UTIL_PACKAGE = "com.bobocode.util";
    private static final String TRIMMED = "Trimmed";
    private static final String ENABLE_STRING_TRIMMING = "EnableStringTrimming";
    private static final String TRIMMED_ANNOTATION_BEAN_POST_PROCESSOR = "TrimmedAnnotationBeanPostProcessor";
    private static final String STRING_TRIMMING_CONFIGURATION = "StringTrimmingConfiguration";

    private Reflections annotationReflections;
    private Reflections configurationReflections;

    @BeforeEach
    @SneakyThrows
    public void init() {
        annotationReflections = new Reflections(ANNOTATIONS_PACKAGE);
        configurationReflections = new Reflections(UTIL_PACKAGE);
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
    void trimmedAnnotationClassIsMarkedByRetentionRuntimePolicy() {
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
    void enableStringTrimmingAnnotationClassIsMarkedByRetentionRuntimePolicy() {
        Optional<Class<?>> annotationClass = getAnnotationClassMarketByRetentionRuntimePolicy(ENABLE_STRING_TRIMMING);

        assertThat(annotationClass).isPresent();
    }

    @Test
    @Order(5)
    @DisplayName("ApplicationConfig class is marked by @Configuration annotation")
    void applicationConfigurationClassExists() {
        Configuration annotation = ApplicationConfig.class.getAnnotation(Configuration.class);

        assertThat(annotation).isNotNull();
    }

    @Test
    @Order(6)
    @DisplayName("Package scanning is configured for service package")
    void applicationComponentScanIsConfiguredForService() {
        ComponentScan annotation = ApplicationConfig.class.getAnnotation(ComponentScan.class);

        Optional<String> scannedPackage = Arrays.stream(annotation.basePackages())
                .filter(p -> p.equals("com.bobocode.service"))
                .findAny();
        assertThat(annotation).isNotNull();
        assertThat(scannedPackage).isPresent();
    }

    @Test
    @Order(7)
    @DisplayName("TextService is marked by @Service annotation")
    void textServiceIcMarkedAsComponent() {
        Service annotation = TextService.class.getAnnotation(Service.class);

        assertThat(annotation).isNotNull();
    }

    @Test
    @Order(8)
    @DisplayName("TextService is marked by @Trimmed annotation")
    void textServiceIcMarkedAsTrimmed() {
        Annotation[] annotations = TextService.class.getAnnotations();

        Optional<String> anyTrimmedAnnotation = Arrays.stream(annotations)
                .map(a -> a.annotationType().getSimpleName())
                .filter(n -> n.equals(TRIMMED))
                .findAny();

        assertThat(anyTrimmedAnnotation).isPresent();
    }

    @Test
    @Order(9)
    @DisplayName("TrimmedAnnotationBeanPostProcessor class exists in util package")
    void trimmedAnnotationBeanPostProcessorClassExists() throws ClassNotFoundException {
        Class.forName(UTIL_PACKAGE + "." + TRIMMED_ANNOTATION_BEAN_POST_PROCESSOR);
    }

    @Test
    @Order(10)
    @DisplayName("TrimmedAnnotationBeanPostProcessor class implements BeanPostProcessor interface")
    void trimmedAnnotationBeanPostProcessorClassImplementsBeanPostProcessor() {
        Set<Class<? extends BeanPostProcessor>> beanPostProcessorsClasses = configurationReflections
                .getSubTypesOf(BeanPostProcessor.class);

        Optional<String> anyTrimmedBeanPostProcessor = beanPostProcessorsClasses.stream()
                .map(Class::getSimpleName)
                .filter(n -> n.equals(TRIMMED_ANNOTATION_BEAN_POST_PROCESSOR))
                .findAny();

        assertThat(anyTrimmedBeanPostProcessor).isPresent();
    }

    @Test
    @Order(11)
    @DisplayName("TrimmedAnnotationBeanPostProcessor class is not marked as Spring bean")
    void trimmedAnnotationBeanPostProcessorClassIsNotMarkedAsBean() {
        Set<Class<? extends BeanPostProcessor>> beanPostProcessorsClasses = configurationReflections
                .getSubTypesOf(BeanPostProcessor.class);

        Optional<Class<? extends BeanPostProcessor>> anyTrimmedBeanPostProcessor = beanPostProcessorsClasses.stream()
                .filter(c -> c.getSimpleName().equals(TRIMMED_ANNOTATION_BEAN_POST_PROCESSOR))
                .findAny();

        boolean componentAnnotationIsPresent = anyTrimmedBeanPostProcessor
                .orElseThrow().isAnnotationPresent(Component.class);
        boolean serviceAnnotationIsPresent = anyTrimmedBeanPostProcessor
                .orElseThrow().isAnnotationPresent(Service.class);

        assertFalse(componentAnnotationIsPresent);
        assertFalse(serviceAnnotationIsPresent);
    }

    @Test
    @Order(12)
    @DisplayName("StringTrimmedConfiguration class exists in \"util\" package")
    void stringTrimmedConfigurationClassExists() throws ClassNotFoundException {
        Class.forName(UTIL_PACKAGE + "." + STRING_TRIMMING_CONFIGURATION);
    }

    @Test
    @Order(13)
    @DisplayName("StringTrimmedConfiguration class is marked by @Configuration annotation")
    void stringTrimmedConfigurationClassIsMarkedByConfiguration() {
        Optional<Class<?>> anyMarkedClass = configurationReflections.getTypesAnnotatedWith(Configuration.class).stream()
                .filter(c -> c.getSimpleName().equals(STRING_TRIMMING_CONFIGURATION))
                .findAny();

        assertThat(anyMarkedClass).isPresent();
    }

    @Test
    @Order(14)
    @DisplayName("StringTrimmedConfiguration class creates TrimmedAnnotationBeanPostProcessor bean using method")
    void stringTrimmedConfigurationClassCreatesTrimmedBeanPostProcessor() throws ClassNotFoundException {
        Class<?> configurationClass = Class.forName(UTIL_PACKAGE + "." + STRING_TRIMMING_CONFIGURATION);

        Optional<Method> optionalMethod = Arrays
                .stream(configurationClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .filter(method -> method.getReturnType().equals(BeanPostProcessor.class))
                .findAny();

        assertThat(optionalMethod).isPresent();
    }

    @Test
    @Order(15)
    @DisplayName("EnableStringAnnotation class is marked with @Configuration")
    void enableStringTrimmingAnnotationClassIsMarkedAsConfiguration() {
        Optional<Class<?>> anyAnnotationClass = annotationReflections
                .getTypesAnnotatedWith(Configuration.class).stream()
                .filter(c -> c.getSimpleName().equals(ENABLE_STRING_TRIMMING))
                .findAny();

        assertThat(anyAnnotationClass).isPresent();
    }

    @Test
    @Order(16)
    @DisplayName("EnableStringAnnotation class is marked with @Configuration")
    void enableStringTrimmingAnnotationClassIsMarkedWithConfiguredImport() {
        Optional<Class<?>> anyAnnotationClass = annotationReflections
                .getTypesAnnotatedWith(Import.class).stream()
                .filter(c -> c.getSimpleName().equals(ENABLE_STRING_TRIMMING))
                .filter(c -> c.getAnnotation(Import.class).value()[0].getSimpleName().equals(STRING_TRIMMING_CONFIGURATION))
                .findAny();

        assertThat(anyAnnotationClass).isPresent();
    }

    @Test
    @Order(17)
    @DisplayName("TextService bean is CGLIB proxy")
    void textServiceHasProxy() {
        String className = textService.getClass().getSimpleName();

        boolean isCGLibCreature = className.contains("CGLIB");

        assertThat(className).isNotEqualTo("TextService");
        assertTrue(isCGLibCreature);
    }

    @Test
    @Order(18)
    @DisplayName("Bean is not proxy when @Trimmed annotation is missed")
    void beanIsNotProxyWhenTrimmedAnnotationIsNotUsed() {
        String name = notTrimmedTextService.getClass().getSimpleName();

        assertThat(name).isEqualTo("NotTrimmedTextService");
    }

    @Test
    @Order(19)
    @DisplayName("TextService receives trimmed String method arguments")
    void textServiceReceivesTrimmedStringMethodArguments() {
        String text = "   Need more space   ";
        textService.saveText(text);

        String result = textService.savedText;

        assertThat(result).isEqualTo("Need more space");
    }

    @Test
    @Order(20)
    @DisplayName("TextService method returns trimmed String value")
    void textServiceMethodReturnsTrimmedStringValue() {
        String result = textService.getAvailableText();

        assertThat(result).isEqualTo("Who cares about tabbing?");
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
