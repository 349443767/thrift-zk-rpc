package com.fb.zk.manager.connection;

import org.apache.curator.framework.CuratorFramework;

public class ZkClientHolder {

	public static final int ZK_CONNECT_TIMEOUT = 500;

	public static final int ZK_RETRY_FREQ = 5000;

	private static CuratorFramework mClient = null;

	public static void setClient(final CuratorFramework client) {
		mClient = client;
		ConnectionControl controlListener = new ConnectionControl();
		mClient.getConnectionStateListenable().addListener(controlListener);
	}


	public static CuratorFramework getClient() {
		return mClient;
	}


}
