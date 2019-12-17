package com.interest.ids.commoninterface.service.notify;

/**
 * 用于做通知的接口，使得在同一个spring管理下可以相互调用
 * @author wq
 *
 */
public interface MyNotifyService {
	/**
	 * 通知做的事情,主要用于在biz中需要dev做的某些事情，就调用这个方法，目前就一个方法,后面如果有扩展再继续定义
	 */
	void advice();
}
