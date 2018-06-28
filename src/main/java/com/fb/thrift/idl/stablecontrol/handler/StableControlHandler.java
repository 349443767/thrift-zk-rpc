package com.fb.thrift.idl.stablecontrol.handler;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StableControlHandler implements com.fb.thrift.idl.stablecontrol.StableControl.Iface {

	private final static Logger logger = LoggerFactory.getLogger(StableControlHandler.class);

	@Override
	public boolean ping() throws TException {
		logger.info("Server Connection Test");

		return true;
	}

}
