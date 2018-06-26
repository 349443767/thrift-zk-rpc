package com.fb.zk.common;

public interface IServiceInstance {

	String getID();

	void setID(String id);

	String getBaseCategory();

	String getServiceName();

	String getVersion();

	String getIP();

	String getPort();

	boolean varifyParams();

}
