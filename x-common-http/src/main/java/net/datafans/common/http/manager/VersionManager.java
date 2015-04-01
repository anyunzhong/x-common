package net.datafans.common.http.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.datafans.common.http.exception.VersionPathNotFoundException;

import org.springframework.stereotype.Component;

@Component
public class VersionManager {

	private final Integer maxVersionNum = 20;

	private Map<String, List<Integer>> versionMap = new HashMap<String, List<Integer>>();

	public void register(String path, Integer versionNum) {
		if (versionMap.get(path) == null) {
			List<Integer> versions = new ArrayList<Integer>();
			// 初始化为1
			for (int i = 0; i < maxVersionNum; i++) {
				versions.add(1);
			}
			versionMap.put(path, versions);
		}

		List<Integer> versions = versionMap.get(path);

		if (versions != null) {
			for (int i = versionNum - 1; i < maxVersionNum; i++) {
				if (versions.get(i) > versionNum) {
					break;
				}
				versions.set(i, versionNum);
			}
		}
	}

	public String getRealPath(String path, Integer versionNum) throws VersionPathNotFoundException {
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		List<Integer> versions = versionMap.get(path);
		if (versions == null) {
			throw new VersionPathNotFoundException();
		}

		if (versionNum < 1) {
			versionNum = 1;
		}
		Integer size = versions.size();
		if (versionNum > size) {
			versionNum = size;
		}
		return path + versions.get(versionNum - 1);
	}

}
