package com.interest.ids.common.project.support;

/**
 * @author sunbx
 * @Description
 * @time 2017年12月4日 上午9:58:35
 */
public class Response {

    private int code;

    private String message;

    private Object results;


    public Response() {
        super();
    }

    public Response(int code, String message, Object results) {
		super();
		this.code = code;
		this.message = message;
		this.results = results;
	}

	public Response(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getResults() {
        return results;
    }

    public Response setResults(Object results) {
        this.results = results;
        return this;
    }
}
