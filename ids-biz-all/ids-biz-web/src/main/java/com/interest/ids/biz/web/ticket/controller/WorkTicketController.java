package com.interest.ids.biz.web.ticket.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.interest.ids.biz.web.utils.DownloadUtil;
import com.interest.ids.common.project.bean.sm.QueryUser;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.ticket.WorkTicket;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;
import com.interest.ids.commoninterface.service.ticket.FirstTicketService;

@Controller
@RequestMapping("/workTicket")
public class WorkTicketController {
	private static final Logger log = LoggerFactory.getLogger(WorkTicketController.class);
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private FirstTicketService firstTicketService;
	@Autowired
	private IUserInfoService userService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	/**
	 * 保存一種工作票
	 * @param ticket
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Response<String> save(@RequestBody WorkTicket ticket, HttpSession session){
		Response<String> response = new Response<>();
		Object obj = session.getAttribute("user");
		if(null == obj)
		{
			log.error("save workticket fail,no user login ");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            return response;
		}
		UserInfo user = (UserInfo)obj;
		
		//查看是否部署過流程，如果沒有部署過則部署一次
		deploy();
		
		//保存业务数据
		ticket.setCreateTime(System.currentTimeMillis());
		ticket.setActState("createFirstWork");
		WorkTicket firstTicket = firstTicketService.insertFirstTicket(ticket);
		//定义activiti变量
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("assignee", user.getId());
		variables.put("issue", ticket.getIssueId());
		variables.put("charge", ticket.getChargeId());
		variables.put("dealType", 1);
		
		//启动流程
		ProcessInstance process = runtimeService.startProcessInstanceByKey("firstWorkProcess", firstTicket.getId().toString(), variables);
		log.info("process start success, process definition id is " + process.getProcessDefinitionId());
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        
		return response;
	}
	
	/**
	 * 根据processDefinitionId查询流程
	 */
	@RequestMapping(value = "/getWorkTicketByDefinitionId", method = RequestMethod.POST)
    @ResponseBody
	public Response<WorkTicket> getWorkTicketByDefinitionId(@RequestBody JSONObject jsonObject)
	{
		Response<WorkTicket> response = new Response<>();
		if(null == jsonObject || (null != jsonObject && !jsonObject.containsKey("definitionId")))
		{
			log.error("get workTicket fail, definitionId is null");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            return response;
		}
		
		//根据processDefinitionId查询流程
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		query.processInstanceId(jsonObject.getString("definitionId"));
		ProcessInstance instance = query.singleResult();
		String businessKey = null;
		if(null == instance)
		{
			HistoricProcessInstanceQuery processQuery = historyService.createHistoricProcessInstanceQuery();
			processQuery.processInstanceId(jsonObject.getString("definitionId"));
			HistoricProcessInstance ins = processQuery.singleResult();
			businessKey = ins.getBusinessKey();
		}else if(null != instance)
		{
			businessKey = instance.getBusinessKey();
		}
		
		//组装业务数据
		if(null != businessKey)
		{
			WorkTicket workTicket = firstTicketService.getWorkTicketById(businessKey);
			if(null != workTicket)
			{
				if(null != businessKey)
				{
					workTicket.setBusinessKey(Long.parseLong(businessKey));
				}
				Map<String,Object> map = userService.selectUserByPrimaryKey(workTicket.getIssueId());
				workTicket.setIssuerName(map != null  && map.get("loginName") != null ? map.get("loginName").toString() : "");
				List<UserInfo> list = userService.selectUserByIds(workTicket.getGroupId() != null ? workTicket.getGroupId().split(",") : new Object[0]);
				if(null != list && list.size() > 0)
				{
					StringBuffer sb = new StringBuffer();
					for (UserInfo userInfo : list) {
						sb.append(userInfo.getLoginName()).append(",");
					}
					workTicket.setGroupNames(sb.toString().substring(0, sb.toString().length() - 1));
				}
			}
			response.setResults(workTicket);
		}
		log.info("process detail data get success, process definition id is " + jsonObject.getString("definitionId"));
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		return response;
	}
	
	/**
	 * 修改流程数据
	 */
	@RequestMapping(value = "/updateWorkTicket", method = RequestMethod.POST)
    @ResponseBody
	public Response<String> updateWorkTicket(@RequestBody WorkTicket ticket)
	{
		Response<String> response = new Response<>();
		if(null != ticket && ticket.getBusinessKey() != null)
		{
			firstTicketService.updateWorkTicket(ticket);
			response.setCode(ResponseConstants.CODE_SUCCESS);
	        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
	        log.info("update success, businessKey id is " + ticket.getBusinessKey());
		}else
		{
			log.error("update workTicket fail, businessKey is null");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		
		return response;
	}
	
	/**
	 * 执行流程节点
	 */
	@RequestMapping(value = "/executeWorkTicket", method = RequestMethod.POST)
    @ResponseBody
	public Response<String> executeWorkTicket(@RequestBody WorkTicket ticket, HttpSession session)
	{
		Response<String> response = new Response<>();
		Object obj = session.getAttribute("user");
		if(null != obj)
		{
			UserInfo user = (UserInfo)obj;
			TaskQuery taskQuery = taskService.createTaskQuery();
			taskQuery.processInstanceId(ticket.getDefinitionId());
			taskQuery.taskAssignee(user.getId()+"");
			Short dealType = ticket.getDealType();
			
			Task task = taskQuery.singleResult();
			if(null != task)
			{
				Map<String,Object> data = new HashMap<>();
				ProcessInstanceQuery processQuery = runtimeService.createProcessInstanceQuery();
				processQuery.processInstanceId(task.getProcessInstanceId());
				ProcessInstance instance = processQuery.singleResult();
				
				if("issueFirstWork".equals(task.getTaskDefinitionKey()) || "licensorSure".equals(task.getTaskDefinitionKey()) || "recipientFirstSure".equals(task.getTaskDefinitionKey()))
				{
					data.put("dealType", dealType);
				}
				if("recipientFirstSure".equals(task.getTaskDefinitionKey()))
				{
					data.put("licensor", ticket.getUserId());
				}
				if("chargeFirstSubmit".equals(task.getTaskDefinitionKey()))
				{
					data.put("recipient", ticket.getUserId());
				}
				
				taskService.complete(task.getId(),data);
				WorkTicket t = new WorkTicket();
				if(dealType== 3 && "issueFirstWork".equals(task.getTaskDefinitionKey()))
				{
					t.setBusinessKey(Long.parseLong(instance.getBusinessKey()));
					t.setActState("giveup");
				}else if("licensorWorkStop".equals(task.getTaskDefinitionKey()))
				{
					t.setBusinessKey(Long.parseLong(instance.getBusinessKey()));
					t.setActState("processEnd");
				}else
				{
					processQuery = runtimeService.createProcessInstanceQuery();
					processQuery.processInstanceId(task.getProcessInstanceId());
					instance = processQuery.singleResult();
					if(null != instance)
					{
						t.setBusinessKey(Long.parseLong(instance.getBusinessKey()));
						t.setActState(instance.getActivityId());
						
					}
				}
				if(null != t.getBusinessKey() && null != t.getActState())
				{
					firstTicketService.updateWorkTicket(t);
				}
				
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("execute task success, task id is " + task.getId());
			}else
			{
				log.error("execute task fail, task is null");
	            response.setCode(ResponseConstants.CODE_FAILED);
	            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		}else
		{
			log.error("execute task fail, no user login");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		
		return response;
	}
	
	/**
	 * 列出相关流程
	 */
	@RequestMapping(value = "/getWorkTicketList", method = RequestMethod.POST)
    @ResponseBody
	public Response<Map<String,Object>> getWorkTicketList(@RequestBody JSONObject jsonParam)
	{
		Response<Map<String,Object>> response = new Response<>();
		if(null != jsonParam && jsonParam.containsKey("index") && jsonParam.containsKey("pageSize"))
		{
			Map<String,Object> condition = new HashMap<>();
			
			setCondition(jsonParam, condition);
			
			Integer index = Integer.parseInt(jsonParam.get("index").toString());
			Integer pageSize = Integer.parseInt(jsonParam.get("pageSize").toString());
			if(jsonParam.containsKey("chargeName") && StringUtils.isNotEmpty(jsonParam.getString("chargeName")))
			{
				QueryUser queryUser = new QueryUser();
				UserInfo user = new UserInfo();
				user.setLoginName(jsonParam.getString("chargeName"));
				queryUser.setUser(user);
				List<Map<String,Object>> userList = userInfoService.selectUserByConditions(queryUser);
				if(null != userList && userList.size() > 0)
				{
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < userList.size(); i++) {
						sb.append(userList.get(i).get("id")).append(",");
					}
					condition.put("chargeId", sb.toString().substring(0, sb.toString().length()-1));
				}else
				{
					Map<String,Object> result = new HashMap<>();
					result.put("list", new ArrayList<>());
					result.put("index", index);
					result.put("pageSize", pageSize);
					result.put("total", 0);
			        response.setResults(result);
			        response.setCode(ResponseConstants.CODE_SUCCESS);
			        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE); 
			        return response;
				}
				
			}
			
			Integer count = firstTicketService.countWorkTicket(condition);
			if(null != count && count > 0)
			{
				Integer maxPage = count % pageSize == 0 ? count % pageSize : (count % pageSize + 1);
				condition.put("start", (index-1)*pageSize);
				condition.put("pageSize", pageSize);
				if(index > maxPage)
				{
					index = maxPage;
					condition.put("start", (maxPage-1)*pageSize);
				}
				
				List<Map<String,Object>> listMap = firstTicketService.queryWorkTicketList(condition);
				Map<String,Object> result = new HashMap<>();
				result.put("list", listMap);
				result.put("index", index);
				result.put("pageSize", pageSize);
				result.put("total", count);
		        response.setResults(result);
				
			}
			response.setCode(ResponseConstants.CODE_SUCCESS);
	        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		}else
		{
			log.error("get workticket page fail, index is null or pageSize is null");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		
		return response;
	}
	
	/**
	 * 导出和打印
	 * @param jsonParam
	 * @param condition
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("application/octet-stream");
		String fileName = null;
		String lang = request.getParameter(ResponseConstants.REQ_LANG);
		try {
			fileName = DownloadUtil.getUrlSpace(ResourceBundleUtil.getBizKey(lang, "workTicketController.first.ticket") + ".docx"); //"第一种工作票详情"
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String definitionId = request.getParameter("definitionId");
		if(null != definitionId)
		{
			//查询流程
			ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
			query.processInstanceId(definitionId);
			ProcessInstance instance = query.singleResult();
			String businessKey = null;
			if(null == instance)
			{
				HistoricProcessInstanceQuery processQuery = historyService.createHistoricProcessInstanceQuery();
				processQuery.processInstanceId(definitionId);
				HistoricProcessInstance ins = processQuery.singleResult();
				businessKey = ins.getBusinessKey();
			}else if(null != instance)
			{
				businessKey = instance.getBusinessKey();
			}
			if(null != businessKey)
			{
				//加载模板数据
				URL basePath = Thread.currentThread().getContextClassLoader().getResource("");
				String wordFileName = null;
				if (StringUtils.equals(lang, ResponseConstants.EN_LANG)) { // 英文模板路径
					wordFileName = basePath.getPath()+ "word/first_en.docx";
				} else { // 中文模板路径
					wordFileName = basePath.getPath()+ "word/first.docx";
				}
				XWPFTemplate template = XWPFTemplate.compile(wordFileName);
				//封装基础数据
				Map<String,Object>  data = firstTicketService.findExportUserInfo(businessKey);
				if(null == data)
				{
					data = new HashMap<>();
				}
				getTaskInfo(definitionId, data, lang);
				
				template.render(data);
				
				try {
					template.write(response.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			log.error("export workticket fail, definitionId is null or type is null or no user login");
		}
	}

	private void getTaskInfo(String definitionId, Map<String, Object> data, String lang) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		taskQuery.processInstanceId(definitionId);
		List<Task> taskList = taskQuery.list();
		if(null != taskList && taskList.size() > 0)
		{
			Task task = null;
			SimpleDateFormat format = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "workTicketController.ticket.time"));//MM-dd-yyyy  HH:mm:ss "yyyy年MM月dd日 HH时mm分ss秒"
			for (int i = 0; i < taskList.size(); i++) {
				task = taskList.get(i);
				if("createFirstWork".equals(task.getTaskDefinitionKey()))
				{
					data.put("createFirstWorkTime", format.format(task.getCreateTime()));
				}
				if("chargeFirstSubmit".equals(task.getTaskDefinitionKey()))
				{
					data.put("chargeFirstSubmitTime", format.format(task.getCreateTime()));
				}
				if("licensorSure".equals(task.getTaskDefinitionKey()))
				{
					data.put("licensorSureTime", format.format(task.getCreateTime()));
					List<UserInfo> userList = userInfoService.selectUserByIds(new Object[]{task.getAssignee()});
					if(null != userList && userList.size() > 0)
					{
						data.put("sureName", userList.get(0).getLoginName());
					}
				}
				if("licensorWorkStop".equals(task.getTaskDefinitionKey()))
				{
					data.put("licensorWorkStopTime", format.format(task.getCreateTime()));
				}
			}
		}else
		{
			HistoricTaskInstanceQuery instanceQuery = historyService.createHistoricTaskInstanceQuery();
			instanceQuery.processInstanceId(definitionId);
			List<HistoricTaskInstance> historicList = instanceQuery.list();
			if(null != historicList && historicList.size() > 0)
			{
				HistoricTaskInstance task = null;
				SimpleDateFormat format = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "workTicketController.ticket.time"));//"yyyy年MM月dd日 HH时mm分ss秒"
				for (int i = 0; i < historicList.size(); i++) {
					task = historicList.get(i);
					if("createFirstWork".equals(task.getTaskDefinitionKey()))
					{
						data.put("createFirstWorkTime", format.format(task.getCreateTime()));
					}
					if("chargeFirstSubmit".equals(task.getTaskDefinitionKey()))
					{
						data.put("chargeFirstSubmitTime", format.format(task.getCreateTime()));
					}
					if("licensorSure".equals(task.getTaskDefinitionKey()))
					{
						data.put("licensorSureTime", format.format(task.getCreateTime()));
						List<UserInfo> userList = userInfoService.selectUserByIds(new Object[]{task.getAssignee()});
						if(null != userList && userList.size() > 0)
						{
							data.put("sureName", userList.get(0).getLoginName());
						}
					}
					if("licensorWorkStop".equals(task.getTaskDefinitionKey()))
					{
						data.put("licensorWorkStopTime", format.format(task.getCreateTime()));
					}
				}
			}
		}
	}

	private void setCondition(JSONObject jsonParam,
			Map<String, Object> condition) {
		if(jsonParam.containsKey("chargeName"))
		{
			condition.put("chargeId", jsonParam.get("chargeName"));
		}
		if(jsonParam.containsKey("startWorkTime"))
		{
			condition.put("startWorkTime", jsonParam.get("startWorkTime"));
		}
		if(jsonParam.containsKey("endWorkTime"))
		{
			condition.put("endWorkTime", jsonParam.get("endWorkTime"));
		}
		if(jsonParam.containsKey("processStatus"))
		{
			condition.put("processStatus", jsonParam.get("processStatus"));
		}if(jsonParam.containsKey("isActive"))
		{
			condition.put("isActive", jsonParam.get("isActive"));
		}
	}
	
	private void deploy() {
		//查看是否部署過流程，如果未部署過則部署一次
		DeploymentQuery query = repositoryService.createDeploymentQuery();
		query.processDefinitionKey("firstWorkProcess");
		Long count = query.count();
		if(null == count || (null != count && count == 0))
		{
			//獲取target目錄
			URL path = Thread.currentThread().getContextClassLoader().getResource("");
			String process_bpmn = "FirstProcess.bpmn";
			String resouce_png = "FirstProcess.png";
			String pngPath = new File(path.getPath(),"diagram/FirstProcess.png").getAbsolutePath();
			String bpmnPath = new File(path.getPath(),"diagram/FirstProcess.bpmn").getAbsolutePath();
			InputStream process_bpmn_in = null;
			InputStream resource_png_in = null;
			try {
				resource_png_in = new FileInputStream(pngPath);
				process_bpmn_in = new FileInputStream(bpmnPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			Deployment deployment = repositoryService.createDeployment().addInputStream(process_bpmn, process_bpmn_in).addInputStream(resouce_png, resource_png_in).deploy();
			log.info("Deployment deploy success, Deployment id is " + deployment.getId());
		}
	}
}
