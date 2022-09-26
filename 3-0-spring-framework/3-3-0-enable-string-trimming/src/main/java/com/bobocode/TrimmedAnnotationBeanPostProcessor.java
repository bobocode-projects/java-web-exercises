package com.bobocode;

import com.bobocode.annotation.Trimmed;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters are marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be automatically trimmed to "Java"
 * if parameter is marked with {@link Trimmed} annotation.
 * <p>
 * In order to enable automatic String trimming, please use {@link EnableStringTrimming}
 */
public class TrimmedAnnotationBeanPostProcessor {
//todo: Implement TrimmedAnnotationBeanPostProcessor according to javadoc
//todo: Extract creating of TrimmedAnnotationBeanPostProcessor bean to StringTrimmingConfiguration config class
//todo: Implement @EnableStringTrimming annotation to import StringTrimmingConfiguration config to another configuration classes
}
