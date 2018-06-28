package com.fb.thrift.server;

import com.fb.thrift.common.IProcessor;
import com.fb.thrift.common.IServerCreator;
import com.fb.thrift.common.TErrDefine;
import com.fb.thrift.idl.stablecontrol.StableControl;
import com.fb.thrift.idl.stablecontrol.handler.StableControlHandler;
import com.fb.thrift.processor.ThriftProcessor;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.server.TThreadedSelectorServer.Args;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class ServerCreator implements IServerCreator, TErrDefine {

	private final static Logger logger = LoggerFactory.getLogger(ServerCreator.class);

	private final static String STABLE_CTRL_API = "StableCrtl";

	private static ServerCreator sInstance = null;

	private static TServer sServer = null;

	private final ArrayList<IProcessor> fProcessorSet;

	private final IProcessor fStableContrlProcess;

	private ServerCreator() {
		this.fProcessorSet = new ArrayList<IProcessor>();
		this.fStableContrlProcess = new ThriftProcessor(STABLE_CTRL_API,
				new StableControl.Processor<StableControl.Iface>(new StableControlHandler()));

	}

	public static ServerCreator getInstance() {
		if (sInstance == null) {
			sInstance = new ServerCreator();
		}
		return sInstance;

	}

	@Override
	public int initProcessorSet(final ArrayList<IProcessor> processorSet) {
		if (processorSet == null || processorSet.isEmpty()) {
			return ERR_PROCESSOR_NULL;
		}
		this.fProcessorSet.clear();
		for (IProcessor item : processorSet) {
			if (this.fProcessorSet.contains(item)) {
				continue;
			} else {
				this.fProcessorSet.add(item);
			}

		}

		if (!this.fProcessorSet.contains(this.fStableContrlProcess)) {
			this.fProcessorSet.add(this.fStableContrlProcess);

		}

		return ERR_NONE;
	}


	@Override
	public int initProcessor(IProcessor processor) {
		if (processor == null) {
			return ERR_PROCESSOR_NULL;
		}

		this.fProcessorSet.clear();
		this.fProcessorSet.add(processor);
		if (!this.fProcessorSet.contains(this.fStableContrlProcess)) {
			this.fProcessorSet.add(this.fStableContrlProcess);

		}
		return ERR_NONE;

	}


	@Override
	public int createServer(int mode, String ip, int port) {
		if (mode == SRV_MODE_TPOOL) {
			return createThreadPoolServer(ip, port);

		} else {
			return ERR_SRV_MODE;

		}

	}


	@Override
	public int startServer() {
		if (sServer == null) {
			return ERR_SRV_NULL;
		}

		if (sServer.isServing()) {
			return ERR_SRV_ALREADY_SERVED;
		}

		sServer.serve();
		return ERR_NONE;
	}


	private int createHSHAServer(String ip, int port) {
		if (sServer != null && sServer.isServing()) {
			return ERR_SRV_ALREADY_SERVED;
		}

		try {
			TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(port);
			THsHaServer.Args thhsArgs = new THsHaServer.Args(tnbSocketTransport);
			TMultiplexedProcessor processor = new TMultiplexedProcessor();
			for (IProcessor item : this.fProcessorSet) {
				processor.registerProcessor(item.getServiceName(), item.getProcessor());

			}
			thhsArgs.processor(processor);
			thhsArgs.transportFactory(new TFramedTransport.Factory());
			sServer = new THsHaServer(thhsArgs);
			return ERR_NONE;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERR_UN_SPECIFIED;

		}


	}


	private int createThreadPoolServer(String ip, int port) {
		if (sServer != null && sServer.isServing()) {
			return ERR_SRV_ALREADY_SERVED;
		}

		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
			TTransportFactory transportFactory = new TFramedTransport.Factory();
			TProtocolFactory proFactory = new TBinaryProtocol.Factory();
			TMultiplexedProcessor processor = new TMultiplexedProcessor();
			for (IProcessor item : this.fProcessorSet) {
				processor.registerProcessor(item.getServiceName(), item.getProcessor());
			}

			Args arg1 = new Args(serverTransport);
			arg1.maxReadBufferBytes = 1024 * 1024 * 50;
			sServer = new TThreadedSelectorServer(
					arg1.protocolFactory(proFactory).transportFactory(transportFactory).processor(processor)
							.workerThreads(SRV_WORKER_THREAD).selectorThreads(SRV_SELECTOR_THREAD)
							.acceptQueueSizePerThread(ACCEPTQUEUE));
			return ERR_NONE;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERR_UN_SPECIFIED;
		}
	}


}
