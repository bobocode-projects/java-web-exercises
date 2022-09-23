package com.bobocode;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bobocode.config.TrimmingEnabledConfig;
import com.bobocode.config.TrimmingNotEnabledConfig;
import com.bobocode.service.TrimmedService;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(classes = TrimmingEnabledConfig.class)
class TrimmingPostProcessorTest {

    @Test
    @Order(1)
    @DisplayName("Annotation @Trimmed has target <PARAMETER>")
    void trimmedAnnotation_hasTypeTarget() {
        Optional<Class<? extends Annotation>> annotation = findAnnotationInPackage("Trimmed");
        Optional<ElementType[]> targetValues = annotation.map(a -> a.getAnnotation(Target.class)).map(Target::value);

        assertTrue(targetValues.isPresent());
        assertEquals(1, targetValues.get().length);
        assertEquals(ElementType.PARAMETER, targetValues.get()[0]);
    }

    @Test
    @Order(2)
    @DisplayName("Annotation @Trimmed has retention <Runtime>")
    void trimmedAnnotation_hasRuntimeRetention() {
        Optional<Class<? extends Annotation>> annotation = findAnnotationInPackage("Trimmed");
        Optional<Retention> retention = annotation.map(a -> a.getAnnotation(Retention.class))
                .filter(r -> r.value().equals(RetentionPolicy.RUNTIME));

        assertTrue(retention.isPresent());
    }

    @Test
    @Order(3)
    @DisplayName("Annotation @EnableStringTrimming has target <Type>")
    void enableStringTrimmingAnnotation_hasCorrectTarget() {
        Optional<Class<? extends Annotation>> annotation = findAnnotationInPackage("EnableStringTrimming");
        Optional<ElementType[]> targetValues = annotation.map(a -> a.getAnnotation(Target.class)).map(Target::value);

        assertTrue(targetValues.isPresent());
        assertEquals(1, targetValues.get().length);
        assertThat(List.of(targetValues.get()), contains(ElementType.TYPE));

    }

    @Test
    @Order(4)
    @DisplayName("Annotation @EnableStringTrimming has retention <Runtime>")
    void enableStringTrimmingAnnotation_hasRuntimeRetention() {
        Optional<Class<? extends Annotation>> annotation = findAnnotationInPackage("EnableStringTrimming");
        Optional<Retention> retention = annotation.map(a -> a.getAnnotation(Retention.class))
                .filter(r -> r.value().equals(RetentionPolicy.RUNTIME));

        assertTrue(retention.isPresent());
    }

    @Test
    @Order(5)
    @DisplayName("Annotation @EnableStringTrimming imports StringTrimmingConfiguration")
    void enableStringTrimmingAnnotation_importsCorrectConfig() {
        Optional<Class<? extends Annotation>> annotation = findAnnotationInPackage("EnableStringTrimming");
        List<Class<?>> importValues = Arrays.stream(annotation
                        .map(a -> a.getAnnotation(Import.class))
                        .map(Import::value)
                        .orElseGet(() -> new Class<?>[0]))
                .toList();
        List<String> importNames = importValues.stream().map(Class::getName).collect(toList());
        assertThat(importNames, contains("StringTrimmingConfiguration"));
    }

    @Test
    @Order(6)
    @DisplayName("StringTrimmingConfiguration does not annotated by @Configuration spring annotation, it should be imported to " +
            "config class by @EnableStringTrimming")
    void stringTrimmingConfiguration_annotatedAsConfiguration() {
        Optional<Class<?>> configClass = findClassInPackage("StringTrimmingConfiguration");
        assertTrue(configClass.isPresent());
        assertNull(configClass.get().getDeclaredAnnotation(Configuration.class));
    }

    @Test
    @Order(7)
    @DisplayName("StringTrimmingConfiguration contains methods to provide new instance of TrimmedAnnotationBeanPostProcessor")
    void stringTrimmingConfiguration_createsBeanOfTrimmedAnnotationPostProcessor() {
        Optional<Class<?>> configClass = findClassInPackage("StringTrimmingConfiguration");

        Method[] methods =
                configClass.orElseThrow(() -> new NoSuchElementException("StringTrimmingConfiguration class is not found"))
                        .getDeclaredMethods();

        Optional<Method> postProcessorBeanMethod =
                Stream.of(methods).filter(m -> m.getReturnType().equals(TrimmedAnnotationBeanPostProcessor.class))
                        .findFirst();
        assertTrue(postProcessorBeanMethod.isPresent());
    }

    @Test
    @Order(8)
    @DisplayName("Methods of StringTrimmingConfiguration that provides TrimmedAnnotationBeanPostProcessor instance annotated with" +
            " @Bean annotation")
    void trimmedAnnotationBeanPostProcessorMethod_markedAsBean() {
        Optional<Class<?>> configClass = findClassInPackage("StringTrimmingConfiguration");
        Method[] methods = configClass.orElseThrow(
                () -> new NoSuchElementException("StringTrimmingConfiguration class is not found")).getDeclaredMethods();
        Optional<Bean> beanAnnotation = Stream.of(methods)
                .filter(m -> m.getReturnType().equals(TrimmedAnnotationBeanPostProcessor.class))
                .map(m -> m.getAnnotation(Bean.class)).findFirst();
        assertTrue(beanAnnotation.isPresent());
    }

    @Test
    @Order(9)
    @DisplayName("TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor interface")
    void trimmedAnnotationBeanPostProcessor_implementsBeanPostProcessor() {
        boolean isBeanPostProcessorPresent = Arrays.stream(TrimmedAnnotationBeanPostProcessor.class.getInterfaces())
                .anyMatch(i -> i.isAssignableFrom(
                        BeanPostProcessor.class));
        assertTrue(isBeanPostProcessorPresent);
    }

    @Test
    @Order(10)
    @DisplayName("TrimmedAnnotationBeanPostProcessor overrides postProcessAfterInitialization() method")
    void trimmedAnnotationBeanPostProcessor_overridesPostProcessAfterInitMethod() {
        List<String> methodNames = Arrays.stream(TrimmedAnnotationBeanPostProcessor.class.getDeclaredMethods())
                .map(Method::getName)
                .toList();
        assertThat(methodNames, contains("postProcessAfterInitialization"));
    }

    @Test
    @Order(11)
    @DisplayName("TrimmedAnnotationBeanPostProcessor does not override postProcessBeforeInitialization() method")
    void trimmedAnnotationBeanPostProcessor_notOverridesPostProcessAfterInitMethod() {
        boolean isBeanPostProcessorPresent = Arrays.stream(TrimmedAnnotationBeanPostProcessor.class.getInterfaces())
                .anyMatch(i -> i.isAssignableFrom(
                        BeanPostProcessor.class));
        if (!isBeanPostProcessorPresent) {
            throw new AssertionError("TrimmedAnnotationBeanPostProcessor must implement BeanPostProcessor interface");
        }

        boolean isBeforeProcessingMethodPresent = Arrays.stream(TrimmedAnnotationBeanPostProcessor.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("postProcessBeforeInitialization"));

        assertFalse(isBeforeProcessingMethodPresent);
    }

    @Test
    @Order(12)
    @DisplayName("TrimmedAnnotationBeanPostProcessor trims String input params marked by @Trimmed if " +
            "trimming is enabled by @EnableStringTrimming ")
    void trimmedAnnotationPostProcessor_annotatedInputParams(@Autowired TrimmedService service) {
        String inputArgs = " Simba Bimba  ";
        String actual = service.getTheTrimmedString(inputArgs);

        assertEquals(inputArgs.trim(), actual);
    }

    @Test
    @Order(13)
    @DisplayName("TrimmedAnnotationBeanPostProcessor not trims String input params that not marked by @Trimmed")
    void trimmedAnnotationPostProcessor_notAnnotatedInputParams(@Autowired TrimmedService service) {
        checkIfBeanPostProcessorImplemented();

        String inputArgs = "  Simba Bimba  ";
        String actual = service.getNotTrimmedString(inputArgs);

        assertEquals(inputArgs, actual);
    }

    @Nested
    @SpringJUnitConfig(classes = TrimmingNotEnabledConfig.class)
    class TrimmingNotEnabledTest {

        @Test
        @DisplayName(
                "TrimmedAnnotationBeanPostProcessor does not trims String input parameters marked by " +
                        "@Trimmed if trimming is not enabled by @EnableStringTrimming")
        void trimmedAnnotationPostProcessor_annotatedInputParams(@Autowired TrimmedService service) {
            checkIfBeanPostProcessorImplemented();
            String inputArgs = "   Mufasa LionKing  ";
            String actual = service.getTheTrimmedString(inputArgs);

            assertEquals(inputArgs, actual);
        }
    }

    private Optional<Class<?>> findClassInPackage(String className) {
        String packageName = TrimmedAnnotationBeanPostProcessor.class.getPackageName();
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .filter(c -> c.getSimpleName().equals(className))
                .findFirst();
    }

    private Optional<Class<? extends Annotation>> findAnnotationInPackage(String annotationName) {
        String packageName = TrimmedAnnotationBeanPostProcessor.class.getPackageName();
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Annotation.class)
                .stream()
                .filter(a -> a.getSimpleName().equals(annotationName))
                .findFirst();
    }

    private void checkIfBeanPostProcessorImplemented() {
        boolean isBeanPostProcessorPresent = Arrays.stream(TrimmedAnnotationBeanPostProcessor.class.getInterfaces())
                .anyMatch(i -> i.isAssignableFrom(
                        BeanPostProcessor.class));

        boolean isPostProcessAfterInitializationOverridden =
                Arrays.stream(TrimmedAnnotationBeanPostProcessor.class.getDeclaredMethods())
                        .map(Method::getName).anyMatch(name -> name.equals("postProcessAfterInitialization"));
        if (!isBeanPostProcessorPresent || isPostProcessAfterInitializationOverridden) {
            throw new AssertionError("TrimmedAnnotationBeanPostProcessor must override postProcessAfterInitialization" +
                    "from BeanPostProcessor interface");
        }
    }
}
