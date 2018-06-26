package com.fb.thrift.client;


import com.fb.thrift.common.IThriftClient;
import com.fb.thrift.common.IThriftClientManager;
import com.fb.thrift.pool.PoolConfig;
import com.fb.thrift.pool.ThriftClientPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

public class ThriftClientManager implements IThriftClientManager {
	private final static Logger logger = LoggerFactory.getLogger(ThriftClientManager.class);

	public static boolean sDebug_Flag = false;

	private static IThriftClientManager sInstance = null;

	private static Hashtable<String, ThriftClientPool> sSrvCache = null;

	private ThriftClientManager() {
		sSrvCache = new Hashtable<String, ThriftClientPool>();
	}

	public static IThriftClientManager getInstance() {
		if (sInstance == null) {
			sInstance = new ThriftClientManager();
		}
		return sInstance;
	}


	public void setDebugFlag(boolean flag) {
		sDebug_Flag = flag;
	}

	public boolean getDebugFlag() {
		return sDebug_Flag;
	}

	@Override
	public void addSrvType(String base_cat, String srv_name, String version, String api_name, String srv_clazz_name) {
		String key = base_cat + srv_name + version;
		if (!sSrvCache.containsKey(key)) {
			sSrvCache.put(key, new ThriftClientPool(base_cat, srv_name, version, api_name, srv_clazz_name,
					PoolConfig.getDefaultPoolConfig()));
		}
	}

	@Override
	public void removeSrvType(String base_cat, String srv_name, String version) {
		String key = base_cat + srv_name + version;
		ThriftClientPool pool = sSrvCache.get(key);
		try {
			pool.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public synchronized IThriftClient getClient(String base_cat, String srv_name, String version, String api_name,
			String srv_clazz_name) {
		String key = base_cat + srv_name + version;
		if (!sSrvCache.containsKey(key)) {
			sSrvCache.put(key, new ThriftClientPool(base_cat, srv_name, version, api_name, srv_clazz_name,
					PoolConfig.getDefaultPoolConfig()));
		}
		return sSrvCache.get(key).getClient();
	}

}
