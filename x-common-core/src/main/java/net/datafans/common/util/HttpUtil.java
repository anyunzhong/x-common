package net.datafans.common.util;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
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

    public static String get(String url) {
        //HttpClient httpClient = getHttpClient();
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
        HttpGet get = new HttpGet(url);

        HttpResponse response;
        String result = null;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            LogUtil.error(HttpUtil.class, e);
        } finally {
            get.releaseConnection();
        }

        return result;
    }

    public static String post(String url, List<NameValuePair> params) {

        HttpClient httpClient = new DefaultHttpClient();
        //HttpClient httpClient = getHttpClient();
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
                    result = EntityUtils.toString(entity);
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

        HttpClient httpClient = new DefaultHttpClient();
        //HttpClient httpClient = getHttpClient();
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
                    result = EntityUtils.toString(entity);
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
