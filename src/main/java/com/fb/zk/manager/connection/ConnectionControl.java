package com.fb.zk.manager.connection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionControl implements ConnectionStateListener {

	private final static Logger logger = LoggerFactory.getLogger(ConnectionControl.class);

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		if (newState == ConnectionState.RECONNECTED) {
			logger.info("ZK Reconnected");
		}


	}


}
