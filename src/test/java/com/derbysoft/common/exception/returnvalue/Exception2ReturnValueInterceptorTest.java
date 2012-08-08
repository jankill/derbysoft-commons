package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class Exception2ReturnValueInterceptorTest {

	@Test
	public void success() throws Throwable {
		Object expected = new Object();
		Exception2ReturnValueHandler<Throwable> exceptionHandler = EasyMock.createMock(Exception2ReturnValueHandler.class);
		MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
		EasyMock.expect(methodInvocation.proceed()).andReturn(expected);
		
		Object[] mocks = new Object[] {methodInvocation};
		EasyMock.replay(mocks);
		
		MethodInterceptor interceptor = new Exception2ReturnValueInterceptor(exceptionHandler);
		Object result = interceptor.invoke(methodInvocation);
		assertEquals(expected, result);
		
		EasyMock.verify(mocks);
	}
	
	@Test
	public void exception() throws Throwable {
		Exception2ReturnValueHandler<Throwable> exceptionHandler = EasyMock.createMock(Exception2ReturnValueHandler.class);
		MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
		Throwable exception = new Exception();
		EasyMock.expect(methodInvocation.proceed()).andThrow(exception);
		Object expected = new Object();
		EasyMock.expect(exceptionHandler.handle(exception, methodInvocation)).andReturn(expected);
		EasyMock.expect(methodInvocation.getMethod()).andReturn(getReturnObjectMethod()).anyTimes();
		
		Object[] mocks = new Object[] {exceptionHandler, methodInvocation};
		EasyMock.replay(mocks);
		
		MethodInterceptor interceptor = new Exception2ReturnValueInterceptor(exceptionHandler);
		Object result = interceptor.invoke(methodInvocation);
		assertEquals(expected, result);
		
		EasyMock.verify(mocks);
	}
	

	
	@Test(expected = IllegalStateException.class)
	public void invalidReturnType() throws Throwable {
		Exception2ReturnValueHandler<Throwable> exceptionHandler = EasyMock.createMock(Exception2ReturnValueHandler.class);
		MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
		Throwable exception = new Exception();
		EasyMock.expect(methodInvocation.proceed()).andThrow(exception);
		Integer invalid = new Integer(1);
		
		Method method = getReturnStringMethod();
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method);
		EasyMock.expect(exceptionHandler.handle(exception, methodInvocation)).andReturn(invalid);
		
		Object[] mocks = new Object[] {exceptionHandler, methodInvocation};
		EasyMock.replay(mocks);
		
		MethodInterceptor interceptor = new Exception2ReturnValueInterceptor(exceptionHandler);
		interceptor.invoke(methodInvocation);
		
		EasyMock.verify(mocks);
	}

	private Method getReturnObjectMethod() {
		return ReflectionUtils.findMethod(Foo.class, "getObject");
	}	
	
	private Method getReturnStringMethod() {
		return ReflectionUtils.findMethod(Foo.class, "getName");
	}
	
	private class Foo {
		
		String getName() {
			return null;
		}
		
		Object getObject() {
			return null;
		}
	}
	
	
}
