package net.datafans.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpUtil {

	private static int CONNECTION_TIMEOUT = 2 * 1000;
	private static int SOCKET_TIMEOUT = 5 * 1000;
	private static int CONNETCION_MANAGER_TIMEOUT = 2 * 1000;

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
				ConnManagerParams.setTimeout(params, CONNETCION_MANAGER_TIMEOUT);

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
			}
		}
		return client;
	}

	public static String get(String url) {
		// HttpClient httpClient = getHttpClient();
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		//httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
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
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (get != null) {
				get.releaseConnection();
			}

		}

		return result;
	}

	public static String post(String url, List<NameValuePair> params) {
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
				UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
