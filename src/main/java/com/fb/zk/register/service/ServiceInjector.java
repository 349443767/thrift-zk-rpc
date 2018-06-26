package com.fb.zk.register.service;

import com.fb.zk.common.IErrDefine;
import com.fb.zk.common.IServiceInjector;
import com.fb.zk.manager.ZkManager;
import com.fb.zk.manager.connection.ZkClientHolder;
import com.fb.zk.register.payload.ZKServiceDetail;
import com.fb.zk.util.xml.JsonSerilizer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceInjector implements IServiceInjector {

	private final static Logger logger = LoggerFactory.getLogger(ServiceInjector.class);

	private ServiceDiscovery<ZKServiceDetail> serviceDiscovery;

	private CuratorFramework mClient = null;

	public ServiceInjector() {

	}

	@Override
	public void shutdown() {

		try {
			if (serviceDiscovery != null) {
				serviceDiscovery.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public int injectService(final ZKServiceDetail instance) {

		mClient = ZkClientHolder.getClient();

		if (mClient == null || !mClient.getZookeeperClient().isConnected()) {
			return IErrDefine.ERR_ZK_CLIENT_STATE_NOT_STARTED;
		}

		try {
			serviceRegist(genServiceInstance(instance));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return IErrDefine.ERR_SERVICE_INJECTED_FAILED;

		}

		return IErrDefine.ERR_NONE;

	}

	private ServiceInstance<ZKServiceDetail> genServiceInstance(ZKServiceDetail instance) throws Exception {

		String base_cat_path = "/" + instance.getBaseCate();
		String srv_name_pth = instance.getServiceName();
		String srv_version_path = instance.getVersion();
		String ip_port_pair = instance.getIP() + ":" + instance.getPort();
		String srv_name = srv_name_pth + "<" + srv_version_path + ">";
		String srv_id = ip_port_pair;
		checkNodeExist(base_cat_path, true);
		serviceDiscoveryStart(base_cat_path);
		ServiceInstance<ZKServiceDetail> ins = ServiceInstance.<ZKServiceDetail>builder().id(srv_id).name(srv_name)
				.address(instance.getIP()).port(Integer.parseInt(instance.getPort()))
				.payload((ZKServiceDetail) instance).build();

		return ins;

	}

	public static void main(String[] args) {
		ZKServiceDetail instance = new ZKServiceDetail("rpc1", "fuckyou111", "1.1", "192.168.1.1", "123123");

		ZkManager.getInstance().initialize("D:/zk_config/");
		ServiceInjector mServiceInjector = new ServiceInjector();
		mServiceInjector.injectService(instance);
		try {
			Thread.sleep(60000);
			logger.info("time up");
			mServiceInjector.shutdown();
			logger.info("remove instance");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void serviceRegist(ServiceInstance<ZKServiceDetail> serviceInstance) throws Exception {
		serviceDiscovery.registerService(serviceInstance);

	}

	@Override
	public int removeService(ZKServiceDetail instance) {
		mClient = ZkClientHolder.getClient();

		if (mClient == null || mClient.getZookeeperClient().isConnected() == false) {
			return IErrDefine.ERR_ZK_CLIENT_STATE_NOT_STARTED;
		}

		try {
			serviceDiscovery.unregisterService(genServiceInstance(instance));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return IErrDefine.ERR_SERVICE_REMOCE_FAILED;

		}
		return IErrDefine.ERR_NONE;

	}

	private void serviceDiscoveryStart(String basePath) throws Exception {
		serviceDiscovery = ServiceDiscoveryBuilder.builder(ZKServiceDetail.class).client(mClient)
				.serializer(JsonSerilizer.getSerializer()).basePath(basePath).build();
		serviceDiscovery.start();
	}


	private boolean checkNodeExist(String path, boolean failThenCreate) throws Exception {
		if (mClient.checkExists().forPath(path) == null) {
			if (failThenCreate) {
				mClient.create().forPath(path);
			}
			return false;
		}
		return true;

	}


}
