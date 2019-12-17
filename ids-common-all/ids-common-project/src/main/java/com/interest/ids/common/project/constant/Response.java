package com.interest.ids.common.project.constant;

import java.io.Serializable;

/**
 * 
 * @Description
 * @author sunbx
 * @time 2017年12月4日 上午9:58:35
 * @param <T>
 */
public class Response<T> implements Serializable {

	private static final long serialVersionUID = 6476276344203847131L;
	private int code;

	private String message;

	private T results;
	

	public Response() {
		super();
	}
	
    public Response(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public Response<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Response<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getResults() {
		return results;
	}

	public Response<T> setResults(T results) {
		this.results = results;
		return this;
	}
}
