package net.datafans.common.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.*;
import java.util.List;

@SuppressWarnings("deprecation")
public class HttpUtil {

    private final static int CONNECTION_TIMEOUT = 2 * 1000;
    private final static int SOCKET_TIMEOUT = 5 * 1000;
    private final static int CONNECTION_MANAGER_TIMEOUT = 2 * 1000;

    private static HttpClient client;

    @Deprecated
    public synchronized static HttpClient getHttpClient() {

        if (client == null) {
            try {

                HttpParams params = new BasicHttpParams();
                // 连接超时
                HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
                // 读取超时
                HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
                // 静态检查
                HttpConnectionParams.setStaleCheckingEnabled(params, true);

                // 连接池数量
                ConnManagerParams.setMaxTotalConnections(params, 1000);
                // 从连接池获取连接超时
                ConnManagerParams.setTimeout(params, CONNECTION_MANAGER_TIMEOUT);

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                client = new DefaultHttpClient(ccm, params);

            } catch (Exception e) {
                LogUtil.error(HttpUtil.class, e);
            }
        }
        return client;
    }


    public synchronized static HttpClient getClient() {


        try {

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
            HttpConnectionParams.setStaleCheckingEnabled(params, true);

//                ConnManagerParams.setMaxTotalConnections(params, 1000);
//                ConnManagerParams.setTimeout(params, CONNECTION_MANAGER_TIMEOUT);
//
//                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//                SchemeRegistry registry = new SchemeRegistry();
//                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//
//                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(params);

        } catch (Exception e) {
            LogUtil.error(HttpUtil.class, e);
        }

        return null;
    }

    public static String get(String url) {
        HttpClient httpClient = getClient();
        HttpGet get = new HttpGet(url);

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity, HTTP.UTF_8);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            get.releaseConnection();
        }

        return result;
    }


    public static byte[] getBytes(String url) {
        HttpClient httpClient = getClient();
        HttpGet get = new HttpGet(url);

        HttpResponse response;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    return IOUtils.toByteArray(inputStream);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            get.releaseConnection();
        }

        return null;
    }

    public static String post(String url, List<NameValuePair> params) {

        HttpClient httpClient = getClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        } catch (UnsupportedEncodingException e) {
            LogUtil.error(HttpUtil.class, e);
        }

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity, HTTP.UTF_8);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            post.releaseConnection();

        }

        return result;
    }


    public static String postFile(String url, String file) {

        HttpClient httpClient = getClient();
        HttpPost post = new HttpPost(url);
        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart("file", new FileBody(new File(file)));
        post.setEntity(multipartEntity);

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity, HTTP.UTF_8);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            post.releaseConnection();

        }

        return result;
    }

    public static String postFileBytes(String url, byte[] bytes) {

        HttpClient httpClient = getClient();
        HttpPost post = new HttpPost(url);
        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart("file", new ByteArrayBody(bytes, "test.jpeg"));
        post.setEntity(multipartEntity);

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity, HTTP.UTF_8);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            post.releaseConnection();

        }

        return result;
    }


    public static String post(String url, String body) {

        HttpClient httpClient = getClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(body, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(HttpUtil.class, e);
        }

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity, HTTP.UTF_8);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            post.releaseConnection();

        }

        return result;
    }


    public static byte[] postByte(String url, String body) {

        HttpClient httpClient = getClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(body, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(HttpUtil.class, e);
        }

        HttpResponse response;
        byte[] result = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = IOUtils.toByteArray(entity.getContent());
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            post.releaseConnection();

        }

        return result;
    }




    public static String post(String url, String body, String encode) {

        HttpClient httpClient = getClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(body, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(HttpUtil.class, e);
        }

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity, encode);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            post.releaseConnection();

        }

        return result;
    }



    static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
