package com.interest.ids.commoninterface.service.ticket;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.ticket.WorkTicket;

public interface FirstTicketService {

	/**保存第一种工作票的业务数据*/
	WorkTicket insertFirstTicket(WorkTicket ticket);

	/**根据id查询业务数据*/
	WorkTicket getWorkTicketById(String businessKey);
	/**更新业务数据*/
	void updateWorkTicket(WorkTicket ticket);
	/**统计流程实例的记录数*/
	Integer countWorkTicket(Map<String, Object> condition);

	/**分页查询流程实例的记录数据*/
	List<Map<String, Object>> queryWorkTicketList(Map<String, Object> condition);

	Map<String, Object> findExportUserInfo(String businessKey);

}
