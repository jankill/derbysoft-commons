package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class Exception2ReturnValueHandlerProxyTest {

	@Test
	public void testHandle() {
		Exception2ReturnValueHandlerRegistry registry = new DefaultException2ReturnValueHandlerRegistry();
		
		final Object expected = new Object();
		
		Exception2ReturnValueHandler<RuntimeException> runtimeExceptionHandler 
			= new Exception2ReturnValueHandler<RuntimeException>() {
				@Override
				public Class<RuntimeException> getThrowableType() {
					return RuntimeException.class;
				}
				@Override
				public Object handle(RuntimeException t, MethodInvocation methodInvocation) {
					return expected;
				}
		};
		
		MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
		registry.registerHandler(runtimeExceptionHandler);
		
		Object[] mocks = new Object[] {methodInvocation};
		EasyMock.replay(mocks);
		
		Exception2ReturnValueHandlerProxy proxy = new Exception2ReturnValueHandlerProxy(registry);
		RuntimeException exception = new RuntimeException();
		Object result = proxy.handle(exception, methodInvocation);
		
		assertSame(expected, result);
		
		EasyMock.verify(mocks);
	}

}
