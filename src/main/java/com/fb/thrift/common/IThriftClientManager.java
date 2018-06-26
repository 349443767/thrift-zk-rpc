package com.fb.thrift.common;


public interface IThriftClientManager {

	void addSrvType(String base_cat, String srv_name, String version, String api_name, String srvClazzName);

	void removeSrvType(String base_cat, String srv_name, String version);

	IThriftClient getClient(String base_cat, String srv_name, String version, String api_name, String srvClazzName);

}
