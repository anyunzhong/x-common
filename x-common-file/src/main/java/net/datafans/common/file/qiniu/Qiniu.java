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


    public Qiniu(String accessKey, String accessSecret, String bucket) {

        this.bucket = bucket;

        auth = Auth.create(accessKey, accessSecret);

    }

    public Response syncUpload(byte[] bytes, String path) throws QiniuException {

        Response res = uploadManager.put(bytes, path, auth.uploadToken(bucket));
        if (res.isOK()) {
            return res;
        }
        return null;

    }
}
