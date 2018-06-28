package com.fb.zk.common.watcher;

import com.fb.zk.common.IWatcherContainer;
import org.apache.zookeeper.Watcher;
import org.omg.CORBA.ServiceDetail;

import java.util.Hashtable;

public class ZKWatcherContainer implements IWatcherContainer {


	private static Hashtable<String, ServiceDetail> sActiveClient;

	private static Hashtable<String, Watcher> sWatcherHolder;

	public ZKWatcherContainer() {
		sActiveClient = new Hashtable<String, ServiceDetail>();
		sWatcherHolder = new Hashtable<String, Watcher>();

	}


	@Override
	public void addWathcer(String product_spec, Watcher watcher) {

	}


	@Override
	public void registThriftClient(String key, ServiceDetail detail) {

	}


}
