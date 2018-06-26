package com.fb.thrift.common;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.transport.TTransport;

public interface IThriftClientFactory {

	TServiceClient createClient(TTransport transport);

}
