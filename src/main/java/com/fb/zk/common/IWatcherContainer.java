package com.fb.zk.common;

import org.apache.zookeeper.Watcher;
import org.omg.CORBA.ServiceDetail;

public interface IWatcherContainer {

	void addWathcer(String product_spec, Watcher watcher);

	void registThriftClient(String key, ServiceDetail detail);
}
