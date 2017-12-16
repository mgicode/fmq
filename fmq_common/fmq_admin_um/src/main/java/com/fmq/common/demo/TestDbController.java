package com.fmq.common.demo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmq.common.base.BaseController;

/**
 * 测试连接mysql
 * 
 * @author ljg
 *
 */
@RestController
@RequestMapping("/mydb")
@SpringBootApplication
public class TestDbController extends BaseController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * http://localhost:8089/mydb/getUsers
	 * 
	 */
	@RequestMapping("/getUsers")
	public List<Map<String, Object>> getDbType() {
		String sql = "select * from UserInfo";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : list) {
			Set<Entry<String, Object>> entries = map.entrySet();
			if (entries != null) {
				Iterator<Entry<String, Object>> iterator = entries.iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					System.out.println(key + ":" + value);
				}
			}
		}
		return list;
	}

	@RequestMapping("/user/{id}")
	public Map<String, Object> getUser(@PathVariable String id) {
		Map<String, Object> map = null;

		List<Map<String, Object>> list = getDbType();

		for (Map<String, Object> dbmap : list) {

			Set<String> set = dbmap.keySet();

			for (String key : set) {
				if ("id".equals(key)) {
					if (dbmap.get(key).equals(id)) {
						map = dbmap;
					}
				}
			}
		}

		if (map == null) {
			map = list.get(0);
		}
		return map;
	}

}
