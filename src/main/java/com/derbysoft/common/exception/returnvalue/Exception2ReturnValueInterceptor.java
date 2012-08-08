package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * @since 2009-3-19
 * @author zhupan
 * @version 1.0
 */
public class Exception2ReturnValueInterceptor implements MethodInterceptor {

    private static Log logger = LogFactory.getLog(Exception2ReturnValueInterceptor.class);

    private Exception2ReturnValueHandler<Throwable> exceptionHandler;

    public Exception2ReturnValueInterceptor(Exception2ReturnValueHandler<Throwable> exceptionHandler) {
        Assert.notNull(exceptionHandler, "exceptionHandler required");
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) {
        try {
            return methodInvocation.proceed();
        } catch (Throwable t) {
            Class<?> returnType = methodInvocation.getMethod().getReturnType();
            Object handled = exceptionHandler.handle(t, methodInvocation);
            if (!returnType.isInstance(handled)) {
                String message = handled + " should be instance of [ " + returnType + " ]";
                logger.error(message);
                throw new IllegalStateException(message);
            }
            String message = String.format(
                    "Global exception handler working : caught an exception [Type : %s, Message : %s] and translated it to [%s], "
                            + "please refer to the following stack trace for more details : ",
                    t.getClass().getName(),
                    t.getMessage(),
                    handled.toString()
            );
            if (logger.isInfoEnabled()) {
                logger.error(message);
            }
            if (logger.isDebugEnabled()) {
                logger.error(t);
            }
            return handled;
        }
    }

}
