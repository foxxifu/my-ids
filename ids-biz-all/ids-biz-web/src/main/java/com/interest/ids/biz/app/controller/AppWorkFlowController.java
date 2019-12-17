package com.interest.ids.biz.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.web.alarm.service.AlarmService;
import com.interest.ids.common.project.bean.alarm.AlarmToDefectVO;
import com.interest.ids.common.project.bean.alarm.QueryWorkFlowDefect;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectDto;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectM;
import com.interest.ids.common.project.bean.alarm.WorkFlowProcessM;
import com.interest.ids.common.project.bean.alarm.WorkFlowTaskM;
import com.interest.ids.common.project.bean.alarm.WorkFlowTaskUserM;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.constant.WorkFlowConstant;
import com.interest.ids.common.project.utils.UUIDUtils;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowDefectService;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowProcessService;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowTaskService;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowTaskUserService;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;

@RequestMapping("/app/appWorkFlowController")
@Controller
public class AppWorkFlowController {
	
	@Resource
	private IWorkFlowDefectService workFlowDefectService;
	@Resource
	private IWorkFlowProcessService workFlowProcessService;
	
	@Resource
	private IWorkFlowTaskUserService workFlowTaskUserService;
	
	@Resource
	private IUserInfoService userMService;
	
	@Autowired
    private AlarmService alarmService;

	@Resource
	private IWorkFlowTaskService workFlowTaskService;
	
	private static final Logger log = LoggerFactory
			.getLogger(AppWorkFlowController.class);
	
	/**
	 * 获取缺陷的4种状态的记录数
	 * @param defectDto
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getWorkFlowCount", method = RequestMethod.POST)
	@ResponseBody
	public Response<WorkFlowDefectDto> getWorkFlowCount(
			@RequestBody WorkFlowDefectDto defectDto, HttpSession session) {
		Response<WorkFlowDefectDto> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (null != defectDto && null != obj) {
			UserInfo user = (UserInfo) obj;
			defectDto.setUserId(user.getId());
			defectDto.setType_(user.getType_());
			WorkFlowDefectDto dto = workFlowDefectService
					.selectWorkFlowCount(defectDto);
			if (null != dto) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(dto);
				log.info("query defect status success");
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("query defect status fail");
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query defect status fail,query condition is null or no user login");
		}
		return response;
	}

	/**
	 * 执行缺陷(待确认->审核 ->确认->复审->消缺)
	 */
	@SuppressWarnings("null")
	@RequestMapping("/executeWorkFlowDefect")
	@ResponseBody
	public Response<WorkFlowDefectDto> executeWorkFlowDefect(
			@RequestBody WorkFlowDefectDto defectDto, HttpSession session) {
		Response<WorkFlowDefectDto> response = new Response<WorkFlowDefectDto>();
		Object obj = session.getAttribute("user");
		if (null != obj && null != defectDto && null != defectDto.getDefectId()
				&& null != defectDto.getProcId()
				&& null != defectDto.getProcState()) {
			UserInfo user = (UserInfo) obj;
			WorkFlowDefectDto d = workFlowDefectService
					.selectWorkFlowDefectMById(defectDto.getDefectId());// 得到当前的缺陷
			WorkFlowProcessM p = workFlowProcessService
					.selectWorkFlowProcessMByProcId(defectDto.getProcId()); // 得到当前的流程
			if("1".equals(defectDto.getOptType()) && WorkFlowConstant.PROC_STATUS_WAITING.equals(defectDto.getProcState())) //代表同意处理该缺陷
			{
				String taskKey = UUIDUtils.getUUID(); // 生成任务key
				String prevTaskId = p.getCurrentTaskId(); // 获取当前的任务id
				defectDto.setTaskContent(null);
				defectDto.setFileId(null);
				// 1. 更新缺陷
				WorkFlowDefectM defect = new WorkFlowDefectM();
				defect.setDefectId(defectDto.getDefectId());
				defect.setProcState(WorkFlowConstant.PROC_STATUS_DEALING);
				defect.setUpdateTime(System.currentTimeMillis());
				workFlowDefectService.updateWorkFlowDefectM(defect); // 更新缺陷表流程状态

				// 2.更新任务
				WorkFlowTaskM task = new WorkFlowTaskM();
				task.setProcId(p.getProcId());
				task.setTaskState(WorkFlowConstant.PROC_STATUS_WAITING);
				task.setOpState(WorkFlowConstant.TASK_OPERATIONED_STATUS);
				task.setOpDesc(defectDto.getOperationDesc());
				task.setTaskEndTime(System.currentTimeMillis());
				workFlowTaskService.updateWorkFlowTaskM(task);// 更新任务表

				// 3. 更新进度
				p.setProcState(WorkFlowConstant.PROC_STATUS_DEALING);
				p.setUpdateTime(System.currentTimeMillis());
				p.setCurrentTaskId(taskKey);
				workFlowProcessService.updateWorkFlowProcess(p); // 更新进度表的进度

				// 4.1. 创建任务关联关系
				defectDto.setStationCode(d.getStationCode());
				WorkFlowTaskUserM taskUser = createWorkFlowTaskUserM(defectDto,
						taskKey, p.getProcId(), user);
				workFlowTaskUserService.insertWorkFlowTaskUserM(taskUser); // 插入任务人员关联关系

				// 4.2.创建处理任务
				task = createWorkFlowTaskM(defectDto, taskKey, p.getProcId(),
						WorkFlowConstant.PROC_STATUS_DEALING,
						WorkFlowConstant.TASK_NOT_OPERATION_STATUS, user);
				task.setPreTaskId(prevTaskId);// 设置前一步任务的id
				workFlowTaskService.insertWorkFlowTaskM(task); // 插入审核任务

				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("defect sure,create task success, defect id is "
						+ defectDto.getDefectId());
			}else if("2".equals(defectDto.getOptType()) && WorkFlowConstant.PROC_STATUS_WAITING.equals(defectDto.getProcState())) //转发该缺陷处理任务
			{
				String taskKey = UUIDUtils.getUUID(); // 生成任务key
				String prevTaskId = p.getCurrentTaskId(); // 获取当前的任务id
				
				//更新任务
				WorkFlowTaskM task = new WorkFlowTaskM();
				task.setProcId(p.getProcId());
				task.setTaskState(WorkFlowConstant.PROC_STATUS_WAITING);
				task.setOpState(WorkFlowConstant.TASK_OPERATIONED_STATUS);
				task.setOpDesc(defectDto.getOperationDesc());
				task.setTaskContent(defectDto.getTaskContent());
				task.setFileId(defectDto.getFileId());
				defectDto.setTaskContent(null);
				defectDto.setFileId(null);
				task.setTaskEndTime(System.currentTimeMillis());
				workFlowTaskService.updateWorkFlowTaskM(task);// 更新任务表
				
				//更新当前任务id
				p.setCurrentTaskId(taskKey);
				workFlowProcessService.updateWorkFlowProcess(p); // 更新进度表的进度
				
				//创建任务关联关系
				WorkFlowTaskUserM taskUser = createWorkFlowTaskUserM(defectDto,
						taskKey, p.getProcId(), user);
				workFlowTaskUserService.insertWorkFlowTaskUserM(taskUser); // 插入任务人员关联关系

				// 3.创建处理任务
				task = createWorkFlowTaskM(defectDto, taskKey,
						p.getProcId(), WorkFlowConstant.PROC_STATUS_WAITING,
						WorkFlowConstant.TASK_NOT_OPERATION_STATUS, user);
				task.setPreTaskId(prevTaskId);
				workFlowTaskService.insertWorkFlowTaskM(task); // 插入审核任务
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			}else if(WorkFlowConstant.PROC_STATUS_DEALING.equals(defectDto.getProcState()))
			{
				String taskKey = UUIDUtils.getUUID(); // 生成任务key
				
				/** 更新缺陷状态表 */
				WorkFlowDefectM defect = new WorkFlowDefectM();
				String fileId = defectDto.getFileId();
				defectDto.setFileId(null);
				defect.setDefectId(defectDto.getDefectId());
				defect.setProcState(WorkFlowConstant.PROC_STATUS_NOT_SURE);
				defect.setUpdateTime(System.currentTimeMillis());
				workFlowDefectService.updateWorkFlowDefectM(defect); // 更新缺陷表流程状态

				/** 更新任务表 */
				String prevTaskId = p.getCurrentTaskId(); // 获取当前的任务id
				WorkFlowTaskM task = new WorkFlowTaskM();
				task.setProcId(p.getProcId());
				task.setTaskState(WorkFlowConstant.PROC_STATUS_DEALING);
				task.setOpState(WorkFlowConstant.TASK_OPERATIONED_STATUS);
				task.setOpDesc(defectDto.getOperationDesc());
				task.setTaskContent(defectDto.getTaskContent());
				task.setFileId(fileId);
				defectDto.setTaskContent(null);
				task.setTaskEndTime(System.currentTimeMillis());
				workFlowTaskService.updateWorkFlowTaskM(task);// 更新任务表

				/** 更新进度表 */
				WorkFlowProcessM process = workFlowProcessService
						.selectWorkFlowProcessMByProcId(defectDto.getProcId()); // 得到当前的流程
				process.setProcState(WorkFlowConstant.PROC_STATUS_NOT_SURE);
				process.setUpdateTime(System.currentTimeMillis());
				process.setCurrentTaskId(taskKey);
				workFlowProcessService.updateWorkFlowProcess(process);

				// 4.1. 创建任务关联关系
				List<UserInfo> userList = userMService.selectUserByIds(new Long[]{d.getCreateUserId()});
				UserInfo u = null;
				if(null != userList && userList.size() > 0)
				{
					u = userList.get(0);
				}else{
					u = new UserInfo();
				}
				defectDto.setStationCode(d.getStationCode());
				WorkFlowTaskUserM taskUser = createWorkFlowTaskUserM(defectDto,
						taskKey, p.getProcId(), user);
				taskUser.setUserId(u.getId());
				workFlowTaskUserService.insertWorkFlowTaskUserM(taskUser); // 插入任务人员关联关系
				// 4.2.创建处理任务
				task = createWorkFlowTaskM(defectDto, taskKey, p.getProcId(),
						WorkFlowConstant.PROC_STATUS_NOT_SURE,
						WorkFlowConstant.TASK_NOT_OPERATION_STATUS, u);
				task.setPreTaskId(prevTaskId);// 设置前一步任务的id
				workFlowTaskService.insertWorkFlowTaskM(task); // 插入审核任务
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("defect dealing  defect id is "
						+ defectDto.getDefectId());
			}else if(WorkFlowConstant.PROC_STATUS_NOT_SURE.equals(defectDto.getProcState()))
			{
				if("1".equals(defectDto.getOptType()))//同意处理完成
				{
					WorkFlowDefectM defect = new WorkFlowDefectM();
					defect.setDefectId(defectDto.getDefectId());
					defect.setProcState(WorkFlowConstant.PROC_STATUS_FINISHED);
					if (WorkFlowConstant.PROC_STATUS_NOT_SURE.equals(defectDto
							.getProcState())) {
						defect.setDealResult(WorkFlowConstant.TASK_DEAL_RESULT_FINISHED);
					} else {
						defect.setDealResult(WorkFlowConstant.TASK_DEAL_RESULT_NOT_DEAL);
					}
					defect.setUpdateTime(System.currentTimeMillis());
					defect.setEndTime(System.currentTimeMillis());
					workFlowDefectService.updateWorkFlowDefectM(defect); // 更新缺陷表流程状态

					/** 更新进度表 */
					WorkFlowProcessM process = workFlowProcessService
							.selectWorkFlowProcessMByProcId(defectDto
									.getProcId()); // 得到当前的流程
					process.setProcState(WorkFlowConstant.PROC_STATUS_FINISHED);
					process.setUpdateTime(System.currentTimeMillis());
					workFlowProcessService.updateWorkFlowProcess(process);
					
					//更新告警状态
					Map<String,Object> condition = new HashMap<>();
					condition.put("ids", d.getAlarmIds());
					condition.put("alarmType", d.getAlarmType());
					condition.put("statusId", "4");
					alarmService.updateAlarm(condition);

					/** 更新任务表 */
					WorkFlowTaskM task = new WorkFlowTaskM();
					task.setProcId(defectDto.getProcId());
					task.setTaskState(WorkFlowConstant.PROC_STATUS_NOT_SURE);
					task.setOpState(WorkFlowConstant.TASK_OPERATIONED_STATUS);
					task.setOpDesc(defectDto.getOperationDesc());
					task.setTaskContent(defectDto.getTaskContent());
					defectDto.setTaskContent(null);
					task.setTaskEndTime(System.currentTimeMillis());
					workFlowTaskService.updateWorkFlowTaskM(task);
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					log.info("defect complete repair  defect id is "
							+ defectDto.getDefectId());
				}else if("2".equals(defectDto.getOptType())) //未完全处理成功，打回去重新处理
				{
					WorkFlowDefectM defect = new WorkFlowDefectM();
					String taskKey = UUIDUtils.getUUID(); // 生成任务key
					String fileId = defectDto.getFileId();
					defectDto.setFileId(null);
					defect.setDefectId(defectDto.getDefectId());
					if (WorkFlowConstant.PROC_STATUS_NOT_SURE.equals(defectDto
							.getProcState())) {
						defect.setProcState(WorkFlowConstant.PROC_STATUS_DEALING);
					} else {
						defect.setProcState(WorkFlowConstant.PROC_STATUS_WAITING);
						defect.setDealResult(WorkFlowConstant.TASK_DEAL_RESULT_NOT_DEAL);
					}
					defect.setDealResult(WorkFlowConstant.TASK_DEAL_RESULT_NOT_START);
					defect.setUpdateTime(System.currentTimeMillis());
					workFlowDefectService.updateWorkFlowDefectM(defect); // 更新缺陷表流程状态

					/** 更新当前任务表 */
					WorkFlowTaskM currentTask = new WorkFlowTaskM();
					currentTask.setProcId(p.getProcId());
					currentTask
							.setTaskState(WorkFlowConstant.PROC_STATUS_NOT_SURE);
					currentTask
							.setOpState(WorkFlowConstant.TASK_OPERATIONED_STATUS);
					currentTask.setOpDesc(defectDto.getOperationDesc());
					currentTask.setTaskEndTime(System.currentTimeMillis());
					currentTask.setTaskContent(defectDto.getTaskContent());
					currentTask.setFileId(fileId);
					defectDto.setTaskContent(null);
					workFlowTaskService.updateWorkFlowTaskM(currentTask);// 更新任务表

					/** 更新进度表 */
					WorkFlowProcessM process = workFlowProcessService
							.selectWorkFlowProcessMByProcId(defectDto
									.getProcId()); // 得到当前的流程
					WorkFlowTaskM task = workFlowTaskService
							.selectWorkFlowTaskMByTaskId(process
									.getCurrentTaskId());
					String currentTaskId = p.getCurrentTaskId(); // 获取当前的任务id
					if (WorkFlowConstant.PROC_STATUS_NOT_SURE.equals(defectDto
							.getProcState())) {
						process.setProcState(WorkFlowConstant.PROC_STATUS_DEALING);
					} else {
						process.setProcState(WorkFlowConstant.PROC_STATUS_WAITING);
					}

					process.setUpdateTime(System.currentTimeMillis());
					process.setCurrentTaskId(taskKey);
					workFlowProcessService.updateWorkFlowProcess(process);

					/** 重新创建一个消缺任务 */
					// 4.2.创建处理任务
					currentTask = workFlowTaskService.selectWorkFlowTaskMByTaskId(currentTaskId);
					WorkFlowTaskUserM taskUser = workFlowTaskUserService.selectWorkFlowTaskUserByPrvTaskId(currentTask.getPreTaskId());
					taskUser.setId(null);
					taskUser.setTaskId(taskKey);
					workFlowTaskUserService.insertWorkFlowTaskUserM(taskUser); // 插入任务人员关联关系
					
					
					WorkFlowTaskM prvtask = workFlowTaskService.selectPrevWorkFlowTaskMById(currentTask.getPreTaskId());
					defectDto.setStationCode(d.getStationCode());
					task = createWorkFlowTaskM(defectDto, taskKey, p.getProcId(),
							WorkFlowConstant.PROC_STATUS_DEALING,
							WorkFlowConstant.TASK_NOT_OPERATION_STATUS, user);
					task.setPreTaskId(currentTaskId);// 设置前一步任务的id
					task.setAssigneeName(prvtask.getAssigneeName());
					task.setTaskState(prvtask.getTaskState());
					workFlowTaskService.insertWorkFlowTaskM(task); // 插入消缺任务
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					log.info("defect not complete repair  defect id is "
							+ defectDto.getDefectId());
					/*WorkFlowTaskM prevTask = new WorkFlowTaskM();
					prevTask.setProcId(p.getProcId());
					prevTask.setTaskState(WorkFlowConstant.TASK_STATE_NOT_FINISHED);
					prevTask.setOpState(WorkFlowConstant.TASK_NOT_OPERATION_STATUS);
					workFlowTaskService.updateWorkFlowTaskM(prevTask);
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					log.info("defect not complete repair  defect id is "
							+ defectDto.getDefectId());*/
				}
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("defect execute fail, defect data is null or no user login");
		}
		return response;
	}
	
	/**
	 * 创建任务人物关联关系
	 * 
	 * @param defectDto
	 * @param taskKey
	 * @param procId
	 * @return
	 */
	private WorkFlowTaskUserM createWorkFlowTaskUserM(
			WorkFlowDefectDto defectDto, String taskKey, String procId,
			UserInfo user) {
		WorkFlowTaskUserM taskUser = new WorkFlowTaskUserM(); // 审核任务关系
		taskUser.setEnterpriseId(defectDto.getEnterpriseId());
		taskUser.setStationCode(defectDto.getStationCode());
		if(null != defectDto.getUserId())
		{
			taskUser.setUserId(defectDto.getUserId());
		}else{
			taskUser.setUserId(user.getId());
		}
		taskUser.setTaskId(taskKey);
		taskUser.setProcId(procId);
		return taskUser;
	}

	/**
	 * 创建任务
	 * 
	 * @param defectDto
	 * @param taskKey
	 * @param procKey
	 * @param taskState
	 * @param taskContent
	 * @param operation
	 * @return
	 */
	private WorkFlowTaskM createWorkFlowTaskM(WorkFlowDefectDto defectDto,
			String taskKey, String procKey, String taskState, String operation,
			UserInfo user) {
		WorkFlowTaskM task = new WorkFlowTaskM();
		task.setTaskId(taskKey);
		task.setTaskKey(taskKey);
		task.setProcId(procKey);
		task.setFileId(defectDto.getFileId());
		task.setTaskStartTime(System.currentTimeMillis());
		task.setTaskState(taskState);
		task.setTaskContent(defectDto.getTaskContent());
		task.setEnterpriseId(defectDto.getEnterpriseId());
		// task.setOperationDesc(defectDto.getOperationDesc());
		task.setTransferorName(defectDto.getTransferorName());// 任务执行人用户名
		if(defectDto.getReviceUserName() != null)
		{
			task.setAssigneeName(defectDto.getReviceUserName());// 任务执行人姓名 --
			// 后面可能需要修改该字段的值
		}else{
			task.setAssigneeName(user.getLoginName());
		}
		// task.setRecipient(defectDto.getReviceUserName());//接收人用户名
		// task.setRecipientName(defectDto.getOwnerId());//接收人姓名
		task.setOpState(WorkFlowConstant.TASK_NOT_OPERATION_STATUS); // 执行操作标记 0
																		// 代表微操作
																		// 1
																		// 代表操作
		task.setStationCode(defectDto.getStationCode());
		task.setEnterpriseId(user.getEnterpriseId());
		return task;
	}
	
	/**
	 * 根据条件查询个人任务信息
	 */
	@RequestMapping(value = "/getWorkFlowTaskByCondition", method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<WorkFlowDefectDto>> getWorkFlowTaskByCondition(
			@RequestBody WorkFlowDefectDto defectDto, HttpSession session) {
		Response<Page<WorkFlowDefectDto>> response = new Response<>();
		Page<WorkFlowDefectDto> page = new Page<>();
		if (null == defectDto.getUserId()) {
			Object obj = session.getAttribute("user");
			if (null != obj) {
				defectDto.setUserId(((UserInfo) obj).getId());
			}
		}
		if (null != defectDto && defectDto.getUserId() != null) {
			QueryWorkFlowDefect condition = new QueryWorkFlowDefect();
			condition.setDefect(defectDto);
			Integer result = workFlowDefectService
					.selectWorkFlowTaskCountByUserId(condition);// 查询总记录数
			page.setIndex(defectDto.getIndex() != null ? defectDto.getIndex()
					: 0);
			page.setPageSize(defectDto.getPageSize() != null ? defectDto
					.getPageSize() : 0);
			if (null == page.getIndex()
					|| (null != page.getIndex() && page.getIndex() < 1)) {
				page.setIndex(1);
			}
			if (null == page.getPageSize()
					|| (null != page.getPageSize() && page.getPageSize() < 15)) {
				page.setPageSize(15);
			}

			// 计算总分页数
			int allSize = result % page.getPageSize() == 0 ? result
					/ page.getPageSize() : result / page.getPageSize() + 1;
			if (page.getIndex() > allSize) {
				page.setPageSize(allSize);
			}

			page.setAllSize(allSize);
			page.setCount(result);
			page.setStart((page.getIndex() - 1) * page.getPageSize());// 计算起始位置

			condition.setPage(page);
			List<WorkFlowDefectDto> list = workFlowTaskService
					.selectTaskByCondition(condition);
			if (null != list) {
				page.setList(list);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("get task by condition success");
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("get task by condition fail");
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("get task by condition fail, query condition is null or no user login");
		}
		response.setResults(page);
		return response;
	}
	
	/**
	 * 根据条件查询缺陷数据
	 */
	@RequestMapping(value = "/getWorkFlowDefectsByCondition", method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<WorkFlowDefectDto>> getWorkFlowDefectsByCondition(
			@RequestBody WorkFlowDefectDto defectDto, HttpSession session) {
		Response<Page<WorkFlowDefectDto>> response = new Response<>();
		Page<WorkFlowDefectDto> page = new Page<>();
		Object obj = session.getAttribute("user");
		if (null != defectDto && null != obj) {
			UserInfo user = (UserInfo) obj;
			defectDto.setUserId(user.getId());
			defectDto.setType_(user.getType_());
			Integer result = workFlowDefectService
					.selectAllWorkFlowDefectMCount(defectDto);// 查询总记录数
			page.setIndex(defectDto.getIndex() != null ? defectDto.getIndex()
					: 0);
			page.setPageSize(defectDto.getPageSize() != null ? defectDto
					.getPageSize() : 0);
			if (null == page.getIndex()
					|| (null != page.getIndex() && page.getIndex() < 1)) {
				page.setIndex(1);
			}
			if (null == page.getPageSize()) {
				page.setPageSize(15);
			}

			// 计算总分页数
			int allSize = result % page.getPageSize() == 0 ? result
					/ page.getPageSize() : result / page.getPageSize() + 1;
			if (page.getIndex() > allSize) {
				page.setPageSize(allSize);
			}

			page.setAllSize(allSize);
			page.setCount(result);
			page.setStart((page.getIndex() - 1) * page.getPageSize());// 计算起始位置

			QueryWorkFlowDefect queryDefect = new QueryWorkFlowDefect();
			queryDefect.setDefect(defectDto);
			queryDefect.setPage(page);

			List<WorkFlowDefectM> list = workFlowDefectService
					.getWorkFowDefectMByConditonAndPage(queryDefect);
			if (null != list) {
				List<WorkFlowDefectDto> l = new ArrayList<>();
				WorkFlowDefectDto dto = null;
				List<String> procIds = new ArrayList<>();
				for (WorkFlowDefectM defect : list) {
					procIds.add(defect.getProcId());
					dto = new WorkFlowDefectDto();
					BeanUtils.copyProperties(defect, dto);
					//查询告警的名称-根据告警类型查询，1为普通告警 2 为只能告警
					if(null != defect.getAlarmType() && null != defect.getAlarmIds())
					{
						List<String> names = workFlowDefectService.getAlarmNameByIds(defect.getAlarmType(), defect.getAlarmIds().split(","));
						if(null != names && names.size() > 0)
						{
							dto.setAlarmNames(StringUtils.join(names, ","));
						}
					}
					l.add(dto);
				}

				// 根据当前的任务id查询当前的任务的处理人的id,和设备名称
				if (null != procIds && procIds.size() > 0) {
					List<Map<String, Object>> listMap = workFlowTaskService
							.selectWorkFlowTaskDealingId(procIds.toArray());
					if (null != listMap && listMap.size() > 0) {
						for (WorkFlowDefectDto d : l) {
							for (Map<String, Object> map : listMap) {
								if (d.getProcId().equals(map.get("procId"))) {
									d.setCurrentUserName(map.get("loginName") != null ? map
											.get("loginName").toString() : "");
									d.setCurrentUserId(map.get("userId") != null ? Long
											.parseLong(map.get("userId")
													.toString()) : -1L);
									d.setDevAlias(map.get("devAlias") != null ? map
											.get("devAlias").toString() : "");
								}
							}
						}
					}
				}
				page.setList(l);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("query defect success");
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("query defect fail");
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query defect fail,query condition is null or no user login");
		}
		response.setResults(page);

		return response;
	}
	
	/**
	 * 查询某一条消缺详情
	 */
	@RequestMapping(value = "/getWorkFlowDefectDetails", method = RequestMethod.POST)
	@ResponseBody
	public Response<WorkFlowDefectDto> getWorkFlowDefectDetails(
			@RequestBody WorkFlowDefectDto defectDto) {
		Response<WorkFlowDefectDto> response = new Response<>();
		if (null != defectDto && null != defectDto.getDefectId()) {
			WorkFlowDefectDto result = workFlowDefectService
					.selectWorkFlowDefectMById(defectDto.getDefectId());
			if (null != result) {
				/*UserInfo u = userMService.selectUserByDefectId(defectDto
						.getDefectId());
				result.setAuditor(u);*/
				result.setTasks(result.getTasks());
				// result.setDeviceType(DevTypeConstant.DEV_TYPE_I18N_ID.get(Integer.parseInt(result.getDeviceType())));
				response.setResults(result);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("query defect detail success, defect id is "
						+ defectDto.getDefectId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("query defect detail fail, defect id is "
						+ defectDto.getDefectId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query defect detail fail, defect id is null ");
		}

		return response;
	}
	
	/**
	 * 查询缺陷关联的告警
	 */
	@RequestMapping(value = "/getDefectAlarm", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<AlarmToDefectVO>> getDefectAlarm(@RequestBody WorkFlowDefectDto defectDto)
	{
		Response<List<AlarmToDefectVO>> response = new Response<>();
		if(null != defectDto && null != defectDto.getAlarmIds()&& null != defectDto.getAlarmType())
		{
			List<AlarmToDefectVO> result = null;
			if(defectDto.getAlarmType().equals("1")) //设备告警
			{
				result = workFlowDefectService.selectAlarmMByAlarmMIds(defectDto.getAlarmIds());
			}else if(defectDto.getAlarmType().equals("2"))//智能告警
			{
				result = workFlowDefectService.selectanalysisByIds(defectDto.getAlarmIds());
			}
			response.setResults(result);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			log.info("query defect alram success,alarm ids is " + defectDto.getAlarmIds() + " alramType is " + defectDto.getAlarmType());
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage("查询关联告警的id为空或告警类型为空");
			log.error("count task by userId fail, userId is null or condition data is null");
		}
		return response;
	}
}
