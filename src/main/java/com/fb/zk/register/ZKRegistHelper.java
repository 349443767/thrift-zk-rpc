package com.fb.zk.register;

import com.fb.zk.common.IErrDefine;
import com.fb.zk.common.IServiceInjector;
import com.fb.zk.common.IZkRegister;
import com.fb.zk.register.payload.ZKServiceDetail;
import com.fb.zk.register.service.ServiceInjector;

public class ZKRegistHelper implements IZkRegister, IErrDefine {

	private static IServiceInjector mServiceInjector = null;

	public ZKRegistHelper() {
		mServiceInjector = new ServiceInjector();
	}

	@Override
	public int registService(ZKServiceDetail detail) {
		return mServiceInjector.injectService(detail);
	}

	@Override
	public int unRegistService(ZKServiceDetail detail) {
		return mServiceInjector.removeService(detail);
	}

}
