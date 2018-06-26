package com.fb.thrift.common;

import java.util.ArrayList;

public interface IServerCreator {

	int SRV_WORKER_THREAD = 256;

	int SRV_SELECTOR_THREAD = 8;

	int ACCEPTQUEUE = 6;

	int SRV_MODE_TPOOL = 100;

	//	  int SRV_MODE_HSHA = 101;

	int initProcessorSet(ArrayList<IProcessor> processorSet);

	int initProcessor(IProcessor processor);

	int createServer(int mode, String ip, int port);

	int startServer();

}
