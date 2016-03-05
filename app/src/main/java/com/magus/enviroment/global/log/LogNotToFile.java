package com.magus.enviroment.global.log;

import android.util.Log;

/**
 * 仅仅在控制台输出日志
 * @author xss
 *
 */
public class LogNotToFile extends LoggerFile.Log4jWrapper {
	 
	private String mTag = "";
	
	public LogNotToFile(String tag){
		mTag = tag;
	}
	
	public void trace(Object message) {
		Log.v(mTag, message.toString());
	}
	
	public void trace(Object message, Throwable t) {
		Log.v(mTag, message.toString(), t);
	}
	
	public void debug(Object message) {
		Log.d(mTag, message.toString());
	}
	
	public void debug(Object message, Throwable t) {
		Log.d(mTag, message.toString(), t);
	}
	
	public void info(Object message) {
		Log.i(mTag, message.toString());
	}
	
	public void info(Object message, Throwable t) {
		Log.i(mTag, message.toString(), t);
	}
	
	public void warn(Object message) {
		Log.w(mTag, message.toString());
	}
	
	public void warn(Object message, Throwable t) {
		Log.w(mTag, message.toString(), t);
	}
	
	public void warn(Throwable t) {
		Log.w(mTag, t);
	}
	
	public void error(Object message) {
		Log.e(mTag, message.toString());
	}
	
	public void error(Object message, Throwable t) {
		Log.e(mTag, message.toString(), t);
	}
	
	public void error(Throwable t) {
		Log.e(mTag, "", t);
	}

	public void fatal(Object message) {
		Log.e(mTag, message.toString());
	}
	
	public void fatal(Object message, Throwable t) {
		Log.e(mTag, message.toString(), t);
	}
	
	public void fatal(Throwable t) {
		Log.e(mTag, "", t);
	}
}
