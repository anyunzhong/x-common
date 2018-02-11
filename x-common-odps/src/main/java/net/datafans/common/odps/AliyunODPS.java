package net.datafans.common.odps;

import java.util.List;

import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordWriter;
import com.aliyun.odps.tunnel.DataTunnel;
import com.aliyun.odps.tunnel.UploadSession;
import net.datafans.common.util.LogUtil;

@SuppressWarnings("deprecation")
public class AliyunODPS {

	private String project;

	private DataTunnel tunnel;

	private Odps odps;

	public Odps getOdps() {
		return odps;
	}

	public AliyunODPS(String accessKey, String accessSecret, String project) {

		this.project = project;

		Account account = new AliyunAccount(accessKey, accessSecret);
		odps = new Odps(account);
		odps.setEndpoint("http://service.odps.aliyun.com/api");
		odps.setDefaultProject(project);
		tunnel = new DataTunnel(odps);
		tunnel.setEndpoint("http://dt.odps.aliyun.com");

	}

	public <T> void insert(String partition, String table, T entity, RecordBuilder<T> builder) {

		try {
			PartitionSpec partitionSpec = new PartitionSpec(partition);
			UploadSession uploadSession = tunnel.createUploadSession(project, table, partitionSpec);

			RecordWriter recordWriter = uploadSession.openRecordWriter(0);
			Record record = uploadSession.newRecord();
			builder.build(record, entity);
			recordWriter.write(record);

			recordWriter.close();
			uploadSession.commit(new Long[] { 0L });
		} catch (Exception e) {
			LogUtil.error(getClass(), e);
		}

	}

	public <T> void batchInsert(String partition, String table, List<T> entities, RecordBuilder<T> builder) {

		try {
			PartitionSpec partitionSpec = new PartitionSpec(partition);
			UploadSession uploadSession = tunnel.createUploadSession(project, table, partitionSpec);

			RecordWriter recordWriter = uploadSession.openRecordWriter(0);

			for (T entity : entities) {
				Record record = uploadSession.newRecord();
				builder.build(record, entity);
				recordWriter.write(record);
			}
			recordWriter.close();
			uploadSession.commit(new Long[] { 0L });
		} catch (Exception e) {
			LogUtil.error(getClass(), e);
		}

	}
}
