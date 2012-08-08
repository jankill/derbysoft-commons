package com.derbysoft.common.exception.returnvalue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractException2ReturnValueHandler<ThrowableType extends Throwable> 
	implements Exception2ReturnValueHandler<ThrowableType> {
	
	protected Class<ThrowableType> throwableType;
	
	public AbstractException2ReturnValueHandler() {
		reflectionGetType();
	}
	
	@SuppressWarnings("unchecked")
	private void reflectionGetType() {
        try {
            Type[] typeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
            throwableType = (Class<ThrowableType>) typeArguments[0];
        } catch (Exception e) {
            throw new RuntimeException("concreate handler should be parameterized!", e);
        }
	}

	@Override
	public Class<ThrowableType> getThrowableType() {
		return throwableType;
	}


	
	
	
}
