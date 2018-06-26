package com.fb.thrift.exception;

public class ThriftException extends RuntimeException {

	public ThriftException() {
	}

	public ThriftException(String message, Throwable cause) {
		super(message, cause);
	}

	public ThriftException(String message) {
		super(message);
	}

}
