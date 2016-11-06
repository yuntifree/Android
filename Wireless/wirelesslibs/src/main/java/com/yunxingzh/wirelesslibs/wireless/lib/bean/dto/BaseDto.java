package com.yunxingzh.wirelesslibs.wireless.lib.bean.dto;

public class BaseDto<T> {
	
	private int errno;

	private String desc;

	private T data;

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
