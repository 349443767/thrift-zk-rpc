package com.fb.thrift.utils;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftUtil {

	private final static Logger logger = LoggerFactory.getLogger(ThriftUtil.class);

	public static void closeClient(TServiceClient client) {
		if (client == null) {
			return;
		}

		try {
			TProtocol proto = client.getInputProtocol();
			if (proto != null) {
				proto.getTransport().close();
			}

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

		try {
			TProtocol proto = client.getOutputProtocol();
			if (proto != null) {
				proto.getTransport().close();
			}

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

	}

}
