package com.bobocode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bobocode.config.TrimmingEnabledConfig;
import com.bobocode.service.NotTrimmedService;
import com.bobocode.service.TrimmedService;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
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
class TrimmingEnabledPostProcessorTest {

    @Test
    @Order(1)
    @DisplayName("Annotation @Trimmed has target <Type>")
    void trimmedAnnotation_hasTypeTarget() {
        Optional<Class<? extends Annotation>> annotation = findAnnotationInPackage("Trimmed");
        Optional<ElementType[]> targetValues = annotation.map(a -> a.getAnnotation(Target.class)).map(Target::value);

        assertTrue(targetValues.isPresent());
        assertEquals(1, targetValues.get().length);
        assertEquals(ElementType.TYPE, targetValues.get()[0]);
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
        assertThat(List.of(targetValues), contains(ElementType.TYPE));

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

        assertThat(importValues, contains(StringTrimmingConfiguration.class));
    }

    @Test
    @Order(6)
    @DisplayName("StringTrimmingConfiguration annotated by @Configuration spring annotation")
    void stringTrimmingConfiguration_annotatedAsConfiguration() {
        Optional<Class<?>> configClass = findClassInPackage("StringTrimmingConfiguration");
        Annotation[] annotations = configClass.getClass().getDeclaredAnnotations();
        assertThat(List.of(annotations), contains(Configuration.class));
    }

    @Test
    @Order(7)
    @DisplayName("StringTrimmingConfiguration contains methods to provide new instance of TrimmedAnnotationBeanPostProcessor")
    void stringTrimmingConfiguration_createsBeanOfTrimmedAnnotationPostProcessor() {
        Optional<Class<?>> configClass = findClassInPackage("StringTrimmingConfiguration");
        Method[] methods = configClass.getClass().getDeclaredMethods();
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
        Method[] methods = configClass.getClass().getDeclaredMethods();
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
        try {
            TrimmedAnnotationBeanPostProcessor.class.getDeclaredMethod(
                    "postProcessAfterInitialization", Object.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Expected that method postProcessAfterInitialization is overridden in " +
                    "TrimmedAnnotationBeanPostProcessor, but was not.");
        }
    }

    @Test
    @Order(11)
    @DisplayName("TrimmedAnnotationBeanPostProcessor trims String input params of all methods of class marked by @Trimmed if " +
            "trimming is enabled by @EnableStringTrimming ")
    void trimmedAnnotationPostProcessor_trimmedClass_inputParams(@Autowired TrimmedService service) {
        String inputArgs = " Simba Bimba  ";
        String actual = service.getTheSameString(inputArgs);

        assertEquals(inputArgs.trim(), actual);
    }

    @Test
    @Order(12)
    @DisplayName("TrimmedAnnotationBeanPostProcessor trims String return values of all methods of class marked by @Trimmed if " +
            "trimming is enabled by @EnableStringTrimming ")
    void trimmedAnnotationPostProcessor_trimmedClass_returnValue(@Autowired TrimmedService service) {
        String inputArgs = "Simba Bimba";
        String actual = service.wrapStringWithSpaces(inputArgs);

        assertEquals(inputArgs, actual);
    }

    @Test
    @Order(13)
    @DisplayName("TrimmedAnnotationBeanPostProcessor not trims String input args methods of class that not marked by @Trimmed")
    void trimmedAnnotationPostProcessor_notTrimmedClass_inputParams(@Autowired NotTrimmedService service) {
        String inputArgs = " Simba Bimba ";
        String actual = service.getTheSameString(inputArgs);

        assertEquals(inputArgs, actual);
    }

    @Test
    @Order(14)
    @DisplayName("TrimmedAnnotationBeanPostProcessor not trims String input args methods of class that not marked by @Trimmed")
    void trimmedAnnotationPostProcessor_notTrimmedClass_returnValue(@Autowired NotTrimmedService service) {
        String inputArgs = "Simba Bimba";
        String actual = service.wrapStringWithSpaces(inputArgs);

        NotTrimmedService outOfContestService = new NotTrimmedService();

        assertEquals(outOfContestService.wrapStringWithSpaces(inputArgs), actual);
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
}
