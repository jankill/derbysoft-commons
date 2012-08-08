package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class AbstractException2ReturnValueHandlerTest {

	@Test
	public void testGetThrowableType() {
		AbstractException2ReturnValueHandler<IllegalArgumentException> handler 
			= new AbstractException2ReturnValueHandler<IllegalArgumentException>() {
				@Override
				public Object handle(IllegalArgumentException t, MethodInvocation methodInvocation) {
					return null;
				}
		};
		
		assertSame(IllegalArgumentException.class, handler.getThrowableType());
	}

}
