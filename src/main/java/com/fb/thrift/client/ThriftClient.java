package com.fb.thrift.client;

import com.fb.thrift.common.IThriftClient;
import com.fb.thrift.idl.stablecontrol.StableControl;
import com.fb.thrift.utils.ThriftUtil;
import com.fb.zk.register.payload.ZKServiceDetail;
import org.apache.commons.pool2.ObjectPool;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThriftClient implements IThriftClient, Closeable {

	private final static Logger logger = LoggerFactory.getLogger(ThriftClient.class);

	private final static String STABLE_CTRL_API = "StableCrtl";

	private final static int TIMEOUT = 3000;

	private final ObjectPool<IThriftClient> fPool;

	private TServiceClient mClient;
	private StableControl.Client mClientConnTest;
	private ZKServiceDetail mSrvDetail;

	private AtomicBoolean mFinish = new AtomicBoolean(false);

	private final String fClazzName;

	private final String fApiName;

	public ThriftClient(ZKServiceDetail detail, String api_name, String srv_clazz_name,
			ObjectPool<IThriftClient> pool) {
		super();
		this.mSrvDetail = detail;
		this.fPool = pool;
		this.fApiName = api_name;
		this.fClazzName = srv_clazz_name;
	}

	@Override
	public ZKServiceDetail getServiceDetail() {
		return mSrvDetail;
	}

	@Override
	public void close() throws IOException {
		try {
			if (!mFinish.get()) {
				this.fPool.returnObject(this);
			} else {
				closeClient();
				this.fPool.invalidateObject(this);
			}
		} catch (Exception e) {
			closeClient();
			logger.error(e.getMessage(), e);
		}
	}

	private void closeClient() {
		ThriftUtil.closeClient(this.mClient);
	}


	@Override
	public void returnClient() throws Exception {
		//if debug flag is on
		//force all cache object to be destroyed
		if (ThriftClientManager.sDebug_Flag) {
			setDestroy();
		}
		close();
	}

	@Override
	public TServiceClient getCLient() {
		return this.mClient;
	}

	@Override
	public void init() throws Exception {
		String clazzClientName = this.fClazzName + "$Client";
		Class clientClazz = Class.forName(clazzClientName);
		String host = this.mSrvDetail.getIP();
		int port = Integer.parseInt(this.mSrvDetail.getPort());

		TTransport transport = new TFramedTransport(new TSocket(host, port, TIMEOUT));

		TProtocol protocol = new TBinaryProtocol(transport);

		TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol, fApiName);

		Class[] argsClass = new Class[]{TProtocol.class};

		Constructor<TServiceClient> cons = clientClazz.getConstructor(argsClass);

		mClient = (TServiceClient) cons.newInstance(mp1);
		if (!transport.isOpen()) {
			transport.open();
		}

	}

	@Override
	public boolean connTest() {
		try {
			String host = this.mSrvDetail.getIP();
			logger.info("host -> " + host);

			int port = Integer.parseInt(this.mSrvDetail.getPort());
			logger.info("port -> " + port);

			TTransport transport = new TFramedTransport(new TSocket(host, port, TIMEOUT));
			TProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol, fApiName);
			mClientConnTest = new StableControl.Client(mp1);
			logger.info("connection open");
			transport.open();

			boolean result = mClientConnTest.ping();
			logger.info("close client");
			ThriftUtil.closeClient(this.mClientConnTest);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

	}

	@Override
	public void setDestroy() {
		this.mFinish.set(true);
	}

}
