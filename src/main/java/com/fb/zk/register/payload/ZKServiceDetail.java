package com.fb.zk.register.payload;

import com.fb.zk.manager.ZkManager;
import org.codehaus.jackson.map.annotate.JsonRootName;


@JsonRootName("details")
public class ZKServiceDetail {

	private String baseCate = null;
	private String serviceName = null;
	private String version = null;
	private String ip = null;
	private String port = null;

	public ZKServiceDetail() {

	}

	public ZKServiceDetail(String base_cate, String service_name, String version, String ip, String port) {

		if (base_cate == null) {
			this.baseCate = ZkManager.DEF_RPC_BASE_CAT;
		} else {
			this.baseCate = base_cate;
		}

		this.serviceName = service_name;
		this.version = version;
		this.ip = ip;
		this.port = port;
	}


	public String getBaseCate() {
		return baseCate;
	}

	public void setBaseCate(String baseCate) {
		this.baseCate = baseCate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String iP) {
		this.ip = iP;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}


}
