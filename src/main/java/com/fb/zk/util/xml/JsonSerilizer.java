package com.fb.zk.util.xml;


import com.fb.zk.register.payload.ZKServiceDetail;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

public class JsonSerilizer {

	private static final JsonInstanceSerializer<ZKServiceDetail> SERIALIZER = new JsonInstanceSerializer<ZKServiceDetail>(
			ZKServiceDetail.class);

	public static JsonInstanceSerializer<ZKServiceDetail> getSerializer() {
		return SERIALIZER;
	}

}
