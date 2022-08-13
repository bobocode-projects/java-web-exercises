package com.bobocode.demo.context;

public interface ApplicationContext {
    <T> T getBean(Class<T> type);

    <T> T getBean(Class<T> type, String name);

    <T> T getAllBeans(Class<T> type);
}
