package net.datafans.common.file.aliyun;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.datafans.common.shutdown.listener.ThreadPoolDestroyListener;
import net.datafans.common.util.ShutdownUtil;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ObjectMetadata;

public class AliyunOSS {

	private OSSClient client;
	private String bucket;


	public AliyunOSS(String url, String accessKey, String accessSecret, String bucket) {
		ClientConfiguration config = new ClientConfiguration();
		client = new OSSClient(url, accessKey, accessSecret, config);
		this.bucket = bucket;
	}

	public Object syncUpload(InputStream input, long size, String path) throws OSSException, ClientException,
			IllegalStateException, IOException {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(size);
		return client.putObject(bucket, path, input, objectMeta);

	}
}
