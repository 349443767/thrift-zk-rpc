package com.fb.zk.common;


import com.fb.zk.register.payload.ZKServiceDetail;

public interface IZkServiceDiscovery {

	ZKServiceDetail getServiceInstance(String base_cat, String service_name, String version);

	void clean();

}
