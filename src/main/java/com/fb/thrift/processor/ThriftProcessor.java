package com.fb.thrift.processor;

import com.fb.thrift.common.IProcessor;
import org.apache.thrift.TProcessor;


public class ThriftProcessor implements IProcessor {

	private final String fSrvApiName;

	private final TProcessor fProcessor;

	public ThriftProcessor(String srv_api_name, TProcessor processor) {
		this.fProcessor = processor;
		this.fSrvApiName = srv_api_name;

	}

	@Override
	public String getServiceName() {
		return fSrvApiName;
	}

	@Override
	public TProcessor getProcessor() {
		return fProcessor;
	}

}
