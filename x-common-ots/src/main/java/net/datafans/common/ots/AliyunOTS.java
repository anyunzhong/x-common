package net.datafans.common.ots;

import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.model.PutRowRequest;

public class AliyunOTS {

	private OTSClient client;

	public AliyunOTS(String url, String accessKey, String accessSecret, String instanceName) {
		client = new OTSClient(url, accessKey, accessSecret, instanceName);
	}

	public void put(PutRowRequest request) {
		client.putRow(request);
	}
}
