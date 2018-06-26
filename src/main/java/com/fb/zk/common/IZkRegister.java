package com.fb.zk.common;


import com.fb.zk.register.payload.ZKServiceDetail;

public interface IZkRegister {

	int registService(ZKServiceDetail detail);

	int unRegistService(ZKServiceDetail detail);

}
