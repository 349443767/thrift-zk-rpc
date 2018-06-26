package com.fb.thrift.common;


import org.apache.thrift.TProcessor;

public interface IProcessor {

	String getServiceName();

	TProcessor getProcessor();

}
