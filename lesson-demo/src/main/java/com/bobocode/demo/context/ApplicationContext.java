package com.bobocode.demo.context;

import java.util.Map;

public interface ApplicationContext {
    <T> T getBean(Class<T> type);

    <T> T getBean(Class<T> type, String name);

    <T> Map<String, T> getAllBeans(Class<T> type);
}
