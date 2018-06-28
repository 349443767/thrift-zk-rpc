package com.fb.zk.discovery;

import com.fb.zk.common.IErrDefine;
import com.fb.zk.common.IZkServiceDiscovery;
import com.fb.zk.manager.ZkManager;
import com.fb.zk.manager.connection.ZkClientHolder;
import com.fb.zk.register.payload.ZKServiceDetail;
import com.fb.zk.util.xml.JsonSerilizer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkServiceDiscovery implements IZkServiceDiscovery, IErrDefine {

	private final static Logger logger = LoggerFactory.getLogger(ZkServiceDiscovery.class);

	private ServiceDiscovery<ZKServiceDetail> mServiceDiscovery;

	private ServiceProvider<ZKServiceDetail> mProvider;

	private String mBaseCat = null;

	private String mBasePath = null;

	private String mSrvName = null;

	public ZkServiceDiscovery() {

	}


	@Override
	public ZKServiceDetail getServiceInstance(String base_cat, String service_name, String version) {
		if (base_cat == null) {
			mBaseCat = ZkManager.DEF_RPC_BASE_CAT;
		} else {
			mBaseCat = base_cat;
		}

		if (service_name == null || version == null) {
			return null;
		}
		mBasePath = "/" + mBaseCat;
		mSrvName = service_name + "<" + version + ">";
		try {
			servicediscoverer();
			ServiceInstance<ZKServiceDetail> instance = getSrvInstance();
			if (instance != null) {
				return instance.getPayload();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}

		return null;
	}


	private void servicediscoverer() throws Exception {
		mServiceDiscovery = ServiceDiscoveryBuilder.builder(ZKServiceDetail.class).client(ZkClientHolder.getClient())
				.serializer(JsonSerilizer.getSerializer()).basePath(mBasePath).build();
		mServiceDiscovery.start();

	}

	private ServiceInstance<ZKServiceDetail> getSrvInstance() throws Exception {
		mProvider = mServiceDiscovery.serviceProviderBuilder().serviceName(mSrvName)
				.providerStrategy(new RandomStrategy<ZKServiceDetail>()).build();
		mProvider.start();
		return mProvider.getInstance();
	}


	@Override
	public void clean() {
		if (mServiceDiscovery == null) {
			return;
		}

		try {
			mServiceDiscovery.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		}
		if (mProvider == null) {
			return;
		}

		try {
			mProvider.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
