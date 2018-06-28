package com.fb.zk.manager;


import com.fb.zk.common.IErrDefine;
import com.fb.zk.common.IZKRegPrefixDefine;
import com.fb.zk.common.IZkRegister;
import com.fb.zk.common.IZkServiceDiscovery;
import com.fb.zk.config.ZKStaticConfigHolder;
import com.fb.zk.config.ZKStaticConfigLoader;
import com.fb.zk.config.entity.ZKServerTargetEntity;
import com.fb.zk.manager.connection.ZkClientHolder;
import com.fb.zk.register.ZKRegistHelper;
import com.fb.zk.util.timer.TimeTaskManager;
import com.fb.zk.discovery.ZkServiceDiscovery;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.Iterator;

public class ZkManager implements IErrDefine, IZKRegPrefixDefine {

	private final static Logger logger = LoggerFactory.getLogger(ZkManager.class);

	private static ZkManager mInstance = null;

	private static IZkRegister mZkRegHelper = null;

	private static ZkServiceDiscovery mSrvDiscovery = null;

	private static TimeTaskManager mTTMgr = null;

	private ZKStaticConfigLoader mZKStaticConfigLoader = null;

	private ZkManager() {
		mZkRegHelper = new ZKRegistHelper();
		mSrvDiscovery = new ZkServiceDiscovery();
		iniTimerTask();

	}

	public static ZkManager getInstance() {
		if (mInstance == null) {
			mInstance = new ZkManager();
		}
		return mInstance;

	}

	public IZkRegister getZkRegisterHelper() {
		return mZkRegHelper;

	}

	public IZkServiceDiscovery getSrvDiscovery() {
		return mSrvDiscovery;

	}

	public TimeTaskManager getTimeTaskManager() {
		return mTTMgr;

	}

	private void iniTimerTask() {
		if (mTTMgr == null) {
			mTTMgr = new TimeTaskManager();
		}
		mTTMgr.initialize();
	}


	public int initialize(String cfg_path) {
		ZKStaticConfigLoader.mTmp_Cfg_Path = cfg_path;
		return initialize();
	}

	private int initialize() {
		if (mZKStaticConfigLoader == null) {
			mZKStaticConfigLoader = new ZKStaticConfigLoader();
		}

		int result = mZKStaticConfigLoader.loadAll();
		if (result != ERR_NONE) {
			return result;
		}

		ZKServerTargetEntity entity = ZKStaticConfigHolder.getZKServerTargetEntity();
		if (entity == null) {
			return IErrDefine.ERR_TARGET_SERVER_EMPTY;
		}
		Hashtable<String, String> serverSet = entity.getTargetList();
		if (serverSet == null || serverSet.isEmpty()) {
			return IErrDefine.ERR_TARGET_SERVER_EMPTY;
		}

		Iterator iterator = serverSet.keySet().iterator();
		String ip = null;
		String port = null;
		String conStr = null;
		CuratorFramework mClient = null;

		while (true) {
			if (!iterator.hasNext()) {
				break;
			}
			ip = iterator.next().toString();
			port = serverSet.get(ip).toString();

			if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
				conStr = ip + ":" + port;
				mClient = CuratorFrameworkFactory.builder().connectString(conStr).namespace("")
						//重连设置
						.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, ZkClientHolder.ZK_RETRY_FREQ))
						.connectionTimeoutMs(ZkClientHolder.ZK_CONNECT_TIMEOUT).build();
				if (mClient == null) {
					continue;
				}
				mClient.start();
				try {
					// need more accurate precedure to handle client connection
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				try {
					mClient.getData().usingWatcher(new Watcher() {

						@Override
						public void process(WatchedEvent event) {
							logger.info("node is changed");
						}

					}).inBackground().forPath("/test");
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				if (mClient.getZookeeperClient().isConnected()) {
					ZkClientHolder.setClient(mClient);
					return IErrDefine.ERR_NONE;
				}

			}

		}

		return IErrDefine.ERR_TARGET_SERVER_CONNECT_FAIL_ALL;
	}


}
