package com.yliu.scheduler.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yliu.utils.RedisUtils;

import redis.clients.jedis.Jedis;

public class Publisher implements Job{
	
	private final static Logger log = LoggerFactory.getLogger(Publisher.class);
	
	
	private long publish(String channel,String message){
		Jedis jedis = RedisUtils.getJedis();
		log.info("发送消息channel={},message={}",channel,message);
		return jedis.publish(channel, message);
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String key = context.getJobDetail().getKey().getName();
		com.yliu.scheduler.tasks.Job job = JobUtils.getJob(key);
		if(job!=null){
			publish(job.getChannel(),job.getMessage());
		}
	}

}
