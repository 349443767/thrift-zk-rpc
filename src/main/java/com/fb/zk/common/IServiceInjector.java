package com.fb.zk.common;


import com.fb.zk.register.payload.ZKServiceDetail;

public interface IServiceInjector {

	int injectService(ZKServiceDetail instance);

	int removeService(ZKServiceDetail instance);

	void shutdown();

}
