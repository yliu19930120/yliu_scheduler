package com.yliu.scheduler.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JobUtils {
	
	private final static Logger log = LoggerFactory.getLogger(JobUtils.class);
			
	public static final Map<String,Job> KEY_JOB_MAP = new HashMap<>();

	static{
		new JobUtils().readJson();
	}
	
	private  void readJson(){
		String json = null;
		
		try {
			 InputStream is = this.getClass().getResourceAsStream("/jobs.json");
			 json = IOUtils.toString(is);
			 JSONObject jsonObj = JSON.parseObject(json);
			 List<Job> jobs = jsonObj.getJSONArray("jobs").toJavaList(Job.class);
			 log.info("读取job数量={}",jobs.size());
			 jobs.forEach(job->JobUtils.KEY_JOB_MAP.put(job.getJobId(), job));
		} catch (IOException e) {
			log.error("配置文件读取错误",e);
		}
	}
	
	public static Job getJob(String jobId){
		if(KEY_JOB_MAP.containsKey(jobId)){
			return KEY_JOB_MAP.get(jobId);
		}
		return null;
	}
	
}
