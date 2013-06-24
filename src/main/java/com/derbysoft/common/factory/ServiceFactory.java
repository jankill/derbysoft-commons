package com.derbysoft.common.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public final class ServiceFactory {

    private static ApplicationContext context;

    private static final ServiceFactory INSTENCE = new ServiceFactory();

    private ServiceFactory() {
    }

    public static void setContext(ApplicationContext context) {
        ServiceFactory.context = context;
    }

    public static ServiceFactory getInstance() {
        return INSTENCE;
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        Map map = context.getBeansOfType(clazz);
        Assert.isTrue(map.size() == 1, "Only single bean of type " + clazz + " allowed");
        return (T) map.values().iterator().next();
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(String serviceName) {
        return (T) context.getBean(serviceName);
    }

}