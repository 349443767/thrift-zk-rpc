package com.fb.thrift.common;


import com.fb.zk.register.payload.ZKServiceDetail;
import org.apache.thrift.TServiceClient;

public interface IThriftClient {

	ZKServiceDetail getServiceDetail();

	TServiceClient getClient();

	void returnClient() throws Exception;

	void setDestroy();

	//	 void finish();
	//
	//	 void setFinish(boolean finish);

	void init() throws Exception;

	boolean connTest();

}
