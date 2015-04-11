package net.datafans.common.queue.httpsqs;

import java.util.ArrayList;
import java.util.List;

import net.datafans.common.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HttpsqsQueue {

	private String queueName;
	private String url;

	public HttpsqsQueue(String url, String queueName) {
		this.queueName = queueName;
		this.url = url;
	}

	public void put(String msg) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", msg));
		HttpUtil.post(url + "?opt=put&name=" + queueName, params);
	}
	
	public void put(String msg,String queueName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", msg));
		HttpUtil.post(url + "?opt=put&name=" + queueName, params);
	}

	public String get() {
		String msg = HttpUtil.get(url + "?opt=get&name=" + queueName);
		if (msg != null && !msg.equals("HTTPSQS_GET_END")) {
			return msg.substring(5);
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
