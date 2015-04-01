package net.datafans.common.file.qiniu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.datafans.common.shutdown.listener.ThreadPoolDestroyListener;
import net.datafans.common.util.ShutdownUtil;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class Qiniu {

	private UploadManager uploadManager = new UploadManager();
	private Auth auth;
	private String bucket;

	private ExecutorService pool;

	public Qiniu(String accessKey, String accessSecret, String bucket) {

		this.bucket = bucket;

		auth = Auth.create(accessKey, accessSecret);

		pool = Executors.newCachedThreadPool();

		ThreadPoolDestroyListener listener = ThreadPoolDestroyListener.sharedInstance();
		listener.addPool(pool);
		ShutdownUtil.sharedInstance().register(listener);
	}

	public Response syncUpload(byte[] bytes, String path) throws QiniuException {

		Response res = uploadManager.put(bytes, path, auth.uploadToken(bucket));
		if (res.isOK()) {
			return res;
		}
		return null;

	}

	public Response asyncUpload(byte[] bytes, String path) {

		pool.execute(new UploadTask(bytes, path));
		return null;
	}

	private class UploadTask implements Runnable {
		private byte[] bytes;
		private String path;

		public UploadTask(byte[] bytes, String path) {
			this.bytes = bytes;
			this.path = path;
		}

		@Override
		public void run() {

			try {
				syncUpload(bytes, path);
			} catch (QiniuException e) {
				e.printStackTrace();
			}

		}

	}
}
