package com.derbysoft.common.exception.returnvalue;

import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public class DefaultException2ReturnValueHandlerRegistry implements Exception2ReturnValueHandlerRegistry, InitializingBean {

    private ConcurrentMap<Class<? extends Throwable>, Exception2ReturnValueHandler<? extends Throwable>> handlers
            = new ConcurrentHashMap<Class<? extends Throwable>, Exception2ReturnValueHandler<? extends Throwable>>();

    @SuppressWarnings("unchecked")
    public Exception2ReturnValueHandler<? extends Throwable> lookup(Class<? extends Throwable> type) {
        Exception2ReturnValueHandler<?> handler = handlers.get(type);
        if (handler != null) {
            return (Exception2ReturnValueHandler<? extends Throwable>) handler;
        }

        if (type == Throwable.class) {
            throw new IllegalStateException("Please register handler for " + Throwable.class);
        } else {
            Class<?> superclass = type.getSuperclass();
            if (!Throwable.class.isAssignableFrom(superclass)) {
                throw new IllegalStateException(type + " is not an invalid throwable concreate class");
            }
            return lookup((Class<? extends Throwable>) superclass);
        }
    }


    public void registerHandler(Exception2ReturnValueHandler<? extends Throwable> handler) {
        Exception2ReturnValueHandler<? extends Throwable> exists = handlers.putIfAbsent(handler.getThrowableType(), handler);
        if (exists != null) {
            throw new IllegalArgumentException("Handler of [ " + handler.getThrowableType()
                    + " ] aready exists : [ " + exists.getClass() + "]");
        }
    }

    public void setHandlers(Collection<Exception2ReturnValueHandler<? extends Throwable>> handlers) {
        for (Exception2ReturnValueHandler<? extends Throwable> handler : handlers) {
            registerHandler(handler);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Exception2ReturnValueHandler<? extends Throwable> throwableHandler = handlers.get(Throwable.class);
        if (throwableHandler == null) {
            throw new IllegalStateException("Please register handler for " + Throwable.class);
        }
    }

}
