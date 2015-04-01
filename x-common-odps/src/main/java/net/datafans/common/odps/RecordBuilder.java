package net.datafans.common.odps;

import com.aliyun.odps.data.Record;

public interface RecordBuilder<T> {
	void build(Record record, T entity);
}
