package net.datafans.common.http.entity;

public class AccessLog {
	private int logId;
	private long accessTime;
	private int timeCost;
	private String path;
	private int apiVersion;
	private int deviceType;
	private String params;
	private String clientHost;
	private int clientUniqueId;
	private String serverId;
	private int errorCode;
	private String errorMsg;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public long getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}

	public int getTimeCost() {
		return timeCost;
	}

	public void setTimeCost(int timeCost) {
		this.timeCost = timeCost;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public int getClientUniqueId() {
		return clientUniqueId;
	}

	public void setClientUniqueId(int clientUniqueId) {
		this.clientUniqueId = clientUniqueId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(int apiVersion) {
		this.apiVersion = apiVersion;
	}

	@Override
	public String toString() {
		return "AccessLog [logId=" + logId + ", accessTime=" + accessTime + ", timeCost=" + timeCost + ", path=" + path
				+ ", apiVersion=" + apiVersion + ", deviceType=" + deviceType + ", params=" + params + ", clientHost="
				+ clientHost + ", clientUniqueId=" + clientUniqueId + ", serverId=" + serverId + ", errorCode="
				+ errorCode + ", errorMsg=" + errorMsg + "]";
	}
	
	

}
