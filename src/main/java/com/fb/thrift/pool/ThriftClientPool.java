package com.fb.thrift.pool;

import com.fb.thrift.client.ThriftClient;
import com.fb.thrift.common.IThriftClient;
import com.fb.thrift.exception.ThriftException;
import com.fb.zk.manager.ZkManager;
import com.fb.zk.register.payload.ZKServiceDetail;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicInteger;


public class ThriftClientPool {

	private final static Logger logger = LoggerFactory.getLogger(ThriftClientPool.class);

	private final PoolConfig fPoolConfig;

	//	private final IThriftClientFactory fClientFactory;

	private final GenericObjectPool<IThriftClient> fPool;

	private static AtomicInteger mCount = new AtomicInteger(0);

	public ThriftClientPool(final String base_cat, final String srv_name, final String version, final String api_name,
			final String srv_clazz_name, PoolConfig config) {

		if (config == null) {
			throw new IllegalArgumentException("config is empty!");
		}
		this.fPoolConfig = config;
		// test if config change

		this.fPool = new GenericObjectPool<IThriftClient>(new BasePooledObjectFactory<IThriftClient>() {
			@Override
			public IThriftClient create() throws Exception {
				ZKServiceDetail detail = null;
				int retryCount = 0;
				mCount.set(mCount.get() + 1);
				while (true) {
					try {
						detail = getSrvDetail(base_cat, srv_name, version, api_name);
						IThriftClient client = new ThriftClient(detail, api_name, srv_clazz_name, fPool);
						client.init();
						return client;
					} catch (Exception e) {
						if (retryCount > 3) {
							return null;
						}
						retryCount++;
						logger.info("SRV CREATE ERROR:  " + e.getMessage(), e);

					}
				}

			}

			@Override
			public PooledObject<IThriftClient> wrap(IThriftClient obj) {
				return new DefaultPooledObject<IThriftClient>(obj);
			}

			@Override
			public boolean validateObject(PooledObject<IThriftClient> p) {
				return super.validateObject(p);
			}

			@Override
			public void destroyObject(PooledObject<IThriftClient> p) throws Exception {
				((Closeable) p.getObject()).close();
				super.destroyObject(p);
			}
		}, fPoolConfig);
	}


	private ZKServiceDetail getSrvDetail(String base_cat, String srv_naem, String version, String api_name) {
		ZKServiceDetail tmpInstance = ZkManager.getInstance().getSrvDiscovery()
				.getServiceInstance(base_cat, srv_naem, version);
		ZkManager.getInstance().getSrvDiscovery().clean();
		return tmpInstance;

	}

	public IThriftClient getClient() throws ThriftException {
		try {
			return this.fPool.borrowObject();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			if (e instanceof ThriftException) {
				throw (ThriftException) e;
			}
			throw new ThriftException("Get client from pool failed.", e);
		}
	}

	public void close() throws Exception {
		this.fPool.close();
	}


}
