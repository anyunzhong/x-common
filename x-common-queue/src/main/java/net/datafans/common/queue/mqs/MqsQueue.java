package net.datafans.common.queue.mqs;

import com.aliyun.mqs.client.AsyncCallback;
import com.aliyun.mqs.client.CloudQueue;
import com.aliyun.mqs.client.DefaultMQSClient;
import com.aliyun.mqs.client.MQSClient;
import com.aliyun.mqs.model.Message;

public class MqsQueue {

	private MQSClient client;

	private String queueName;

	public MqsQueue(String url, String key, String secret, String queueName) {
		client = new DefaultMQSClient(url, key, secret);
		this.queueName = queueName;
	}

	public void put(String msg) {
		try {
			CloudQueue queue = client.getQueueRef(queueName);
			Message message = new Message();
			message.setMessageBody(msg);
			queue.asyncPutMessage(message, new AsyncCallback<Message>() {

				@Override
				public void onSuccess(Message result) {
				}

				@Override
				public void onFail(Exception ex) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get() {
		try {
			CloudQueue queue = client.getQueueRef(queueName);
			Message message = queue.popMessage();
			if (message != null) {
				queue.deleteMessage(message.getReceiptHandle());
				return message.getMessageBodyAsString();
			} else {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
