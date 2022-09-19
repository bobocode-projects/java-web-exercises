package com.bobocode;

import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans marked with {@link Trimmed} annotation, creates
 * proxy of them, overrides all methods and trims all {@link String} arguments and return values.
 * For example if there is a string " Java   " as an input parameter or value to return from a method in the class marked by
 * {@link Trimmed} annotation, it should be automatically  trimmed to "Java".
 */
//todo: Implement TrimmedAnnotationBeanPostProcessor according to javadoc
//todo: Implement @Trimmed annotation
//todo: Extract creating of TrimmedAnnotationBeanPostProcessor bean to StringTrimmingConfiguration config class
//todo: Implement @EnableStringTrimming annotation to import StringTrimmingConfiguration config to another configuration classes
public class TrimmedAnnotationBeanPostProcessor {

}
