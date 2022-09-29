package com.bobocode;

import com.bobocode.annotation.Trimmed;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters are marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be automatically trimmed to "Java"
 * if parameter is marked with {@link Trimmed} annotation.
 * <p>
 *
 * Note! This bean is not marked as a {@link Component} to avoid automatic scanning, instead it should be created in
 * {@link StringTrimmingConfiguration} class which can be imported to a {@link Configuration} class by annotation
 * {@link EnableStringTrimming}
 */
public class TrimmedAnnotationBeanPostProcessor {
//todo: Implement TrimmedAnnotationBeanPostProcessor according to javadoc
}
