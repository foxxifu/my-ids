package com.interest.ids.biz.web.ticket.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.alarm.AlarmToDefectVO;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.ticket.WorkTicket;
import com.interest.ids.common.project.mapper.ticket.WorkTicketMapper;
import com.interest.ids.commoninterface.dao.alarm.WorkFlowDefectMapper;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;
import com.interest.ids.commoninterface.service.ticket.FirstTicketService;

@Service
public class FirstTicketServiceImpl implements FirstTicketService{

	@Autowired
	private WorkTicketMapper workTicketMapper;
	
	@Autowired
	private WorkFlowDefectMapper workFlowDefectMapper;
	
	@Autowired
	private TaskService taskService;
	
	@Resource
	private IUserInfoService userInfoService;
	@Autowired
	private IEnterpriseInfoService enterpriseService;
	
	@Override
	public WorkTicket insertFirstTicket(WorkTicket ticket) 
	{
		workTicketMapper.insertFirstTicket(ticket);
		return ticket;
	}
	@Override
	public WorkTicket getWorkTicketById(String businessKey) {
		
		WorkTicket ticket = workTicketMapper.getWorkTicketById(Long.parseLong(businessKey));
		if(null != ticket && null != ticket.getAlarmType() && null != ticket.getAlarmIds())
		{
			List<AlarmToDefectVO> list = null;
			if(ticket.getAlarmType().equals("1")) //设备告警
			{
				list = workFlowDefectMapper.selectAlarmMByAlarmMIds(ticket.getAlarmIds());
			}else if(ticket.getAlarmType().equals("2"))//智能告警
			{
				list = workFlowDefectMapper.selectanalysisByIds(ticket.getAlarmIds());
			}
			if(null != list && list.size() > 0)
			{
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					sb.append(list.get(i).getAlarmName()).append(",");
				}
				ticket.setAlarmNames(sb.toString().substring(0, sb.toString().length()-1));
			}
		}
		return ticket;
	}
	@Override
	public void updateWorkTicket(WorkTicket ticket) {
		workTicketMapper.updateWorkTicket(ticket);
	}
	@Override
	public Integer countWorkTicket(Map<String, Object> condition) {
		return workTicketMapper.countWorkTicket(condition);
	}
	@Override
	public List<Map<String, Object>> queryWorkTicketList(
			Map<String, Object> condition) {
		
		List<Map<String, Object>> list = workTicketMapper.queryWorkTicketList(condition);
		
		//查询告警数据
		Map<String, Object> temp = null;
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i);
			
			//查询当前处理人
			TaskQuery taskQuery = taskService.createTaskQuery();
			taskQuery.processInstanceId(temp.get("definitionId").toString());
			
			List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().list();
			if(null != tasks && tasks.size() > 0)
			{
				String userId = tasks.get(0).getAssignee();
				if(StringUtils.isNotEmpty(userId))
				{
					List<UserInfo> users = userInfoService.selectUserByIds(new Object[]{userId});
					if(null != users && users.size() > 0)
					{
						temp.put("processPerson", users.get(0).getLoginName());
						temp.put("processUserId", userId);
					}
				}
			}
			
			//查询关联告警数据
			if(null != temp.get("alarmIds") && null != temp.get("alarmType"))
			{
				String alarmIds = temp.get("alarmIds").toString();
				String alarmType = temp.get("alarmType").toString();
				List<AlarmToDefectVO> result = null;
				if(alarmType.equals("1")) //设备告警
				{
					result = workFlowDefectMapper.selectAlarmMByAlarmMIds(alarmIds);
				}else if(alarmType.equals("2"))//智能告警
				{
					result = workFlowDefectMapper.selectanalysisByIds(alarmIds);
				}
				
				if(null != result && result.size() > 0)
				{
					StringBuffer sb = new StringBuffer();
					for (int j = 0; j < result.size(); j++) {
						sb.append(result.get(0).getAlarmName()).append(",");
					}
					
					temp.put("alarmNames", sb.toString().substring(0, sb.toString().length()-1));
				}
			}
		}
		
		return list;
	}
	@Override
	public Map<String, Object> findExportUserInfo(String businessKey) {
		if(null != businessKey)
		{
			WorkTicket ticket = workTicketMapper.getWorkTicketById(Long.parseLong(businessKey));
			if(null != ticket)
			{
				Map<String,Object> data = new HashMap<>();
				getUserInfo(ticket, data);
				Map<String,Object> stationInfo = workTicketMapper.selectStationAndDevInfo(ticket.getDevId());
				if(null != stationInfo && stationInfo.size() > 0)
				{
					data.put("stationInfo", "电站名称为: " + stationInfo.get("stationName") + " 设备名称为: " + stationInfo.get("devAlias"));
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
				data.put("startWorkTime", format.format(new Date(ticket.getStartWorkTime())));
				data.put("endWorkTime", format.format(new Date(ticket.getEndWorkTime())));
				data.put("createTime", format.format(new Date(ticket.getCreateTime())));
				data.put("safetyTechnical", ticket.getSafetyTechnical());
				data.put("personalSafetyTechnical", ticket.getPersonalSafetyTechnical());
				data.put("groundWire", ticket.getGroundWire());
				data.put("safetyWarningSigns", ticket.getSafetyWarningSigns());
				data.put("liveElectrifiedEquipment", ticket.getLiveElectrifiedEquipment());
				data.put("workAddress", ticket.getWorkAddress());
				return data;
			}
		}
		
		return null;
	}
	private void getUserInfo(WorkTicket ticket, Map<String, Object> data) {
		List<UserInfo> userList = userInfoService.selectUserByIds(new Object[]{ticket.getChargeId()});
		if(null != userList && userList.size() > 0)
		{
			data.put("chargeName", userList.get(0).getLoginName()); //负责人的名字
			EnterpriseInfo enterprise = enterpriseService.selectEnterpriseMById(userList.get(0).getEnterpriseId()); //查询企业
			if(null != enterprise)
			{
				data.put("enterpriseName", enterprise.getName());
			}
			userList = null;
		}
		if(null != ticket.getGroupId()) //查询组员的名字
		{
			userList = userInfoService.selectUserByIds(ticket.getGroupId().split(","));
			if(null != userList && userList.size() > 0)
			{
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < userList.size(); i++) {
					sb.append(userList.get(i).getLoginName()).append(",");
				}
				data.put("groupNames", sb.toString().substring(0, sb.toString().length()-1));
				data.put("groupNumber", userList.size());
				userList = null;
			}
		}
		if(null != ticket.getIssueId())
		{
			userList = userInfoService.selectUserByIds(new Object[]{ticket.getIssueId()});
			if(null != userList && userList.size() > 0)
			{
				data.put("issuerName", userList.get(0).getLoginName());
			}
		}
	}
}
