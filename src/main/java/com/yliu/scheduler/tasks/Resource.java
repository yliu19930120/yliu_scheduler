package com.yliu.scheduler.tasks;

import java.io.InputStream;

public class Resource {
	
	public InputStream gerResourceInput(String fileName){
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}
}
