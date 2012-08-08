package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class RegistryBasedException2ReturnValueHandlerTest {

	@Test
	public void testHandle() {
		Exception2ReturnValueHandlerRegistry registry = new DefaultException2ReturnValueHandlerRegistry();
		RegistryBasedException2ReturnValueHandler<Exception> wrappedExceptionHandler 
			= new RegistryBasedException2ReturnValueHandler<Exception>(registry) {
				@Override
				protected Throwable getTargetException(Exception t) {
					return t.getCause();
				}
		};
		
		registry.registerHandler(wrappedExceptionHandler);
		
		final Object expected = new Object();
		
		Exception2ReturnValueHandler<IllegalArgumentException> causeExceptionHandler 
			= new AbstractException2ReturnValueHandler<IllegalArgumentException>() {			
				@Override
				public Object handle(IllegalArgumentException t, MethodInvocation methodInvocation) {
					return expected;
				}
		};
		
		registry.registerHandler(causeExceptionHandler);
		
		Exception nestedException = new Exception(new IllegalArgumentException("test"));
		
		MethodInvocation methodInvocation = null;
		Object result = wrappedExceptionHandler.handle(nestedException, methodInvocation);
	
		assertSame(expected, result);
	}

}
