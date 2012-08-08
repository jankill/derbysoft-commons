package com.derbysoft.common.exception.returnvalue;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * @since 2009-3-19
 * @author zhupan
 * @version 1.0
 */
public class DefaultException2ReturnValueHandlerRegistryTest {
	
	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void keyUnique() {
		DefaultException2ReturnValueHandlerRegistry compositeHandler = new DefaultException2ReturnValueHandlerRegistry();
		Exception2ReturnValueHandler<Throwable> throwableHandler = EasyMock.createMock(Exception2ReturnValueHandler.class);
		
		EasyMock.expect(throwableHandler.getThrowableType()).andReturn(Throwable.class).anyTimes();
		
		EasyMock.replay(throwableHandler);
		
		compositeHandler.registerHandler(throwableHandler);
		compositeHandler.registerHandler(throwableHandler);
	
		EasyMock.verify(throwableHandler);
	}
	
	@Test(expected = IllegalStateException.class)
	public void assertThrowableHandlerRegistered() {
		DefaultException2ReturnValueHandlerRegistry registry = new DefaultException2ReturnValueHandlerRegistry();
		registry.afterPropertiesSet();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void singleHandlerRegistered() {
		DefaultException2ReturnValueHandlerRegistry registry = new DefaultException2ReturnValueHandlerRegistry();
		Exception2ReturnValueHandler<RuntimeException> runtimeExceptionHandler 
			= EasyMock.createMock(Exception2ReturnValueHandler.class);
		EasyMock.expect(runtimeExceptionHandler.getThrowableType()).andReturn(RuntimeException.class).anyTimes();
		
		EasyMock.replay(runtimeExceptionHandler);
		
		registry.registerHandler(runtimeExceptionHandler);
		Exception2ReturnValueHandler<? extends Throwable> lookuped = registry.lookup(runtimeExceptionHandler.getThrowableType());
		assertSame(runtimeExceptionHandler, lookuped);
		
		EasyMock.verify(runtimeExceptionHandler);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void directParentExceptionHandlerRegisted() {
		DefaultException2ReturnValueHandlerRegistry registry = new DefaultException2ReturnValueHandlerRegistry();
		Exception2ReturnValueHandler<Exception> parentHandler = EasyMock.createMock(Exception2ReturnValueHandler.class);
		
		EasyMock.expect(parentHandler.getThrowableType()).andReturn(Exception.class).anyTimes();
		
		EasyMock.replay(parentHandler);
		
		registry.registerHandler(parentHandler);
		Class<RuntimeException> childExceptionType = RuntimeException.class;
		Exception2ReturnValueHandler<? extends Throwable> lookuped = registry.lookup(childExceptionType);
		assertSame(parentHandler, lookuped);
		
		EasyMock.verify(parentHandler);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void topLevelExceptionHandlerRegisted() {
		DefaultException2ReturnValueHandlerRegistry registry = new DefaultException2ReturnValueHandlerRegistry();
		Exception2ReturnValueHandler<Throwable> topLevelHandler = EasyMock.createMock(Exception2ReturnValueHandler.class);
		
		EasyMock.expect(topLevelHandler.getThrowableType()).andReturn(Throwable.class).anyTimes();
		
		EasyMock.replay(topLevelHandler);
		
		registry.registerHandler(topLevelHandler);
		Class<RuntimeException> childExceptionType = RuntimeException.class;
		Exception2ReturnValueHandler<? extends Throwable> lookuped = registry.lookup(childExceptionType);
		assertSame(topLevelHandler, lookuped);
		
		EasyMock.verify(topLevelHandler);
	}
	
	
	
}
