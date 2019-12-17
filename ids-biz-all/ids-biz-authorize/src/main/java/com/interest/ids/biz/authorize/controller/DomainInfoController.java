package com.interest.ids.biz.authorize.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.authorize.dto.DomainInfoDto;
import com.interest.ids.common.project.bean.TreeModel;
import com.interest.ids.common.project.bean.sm.DomainInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.IDomainInfoService;

@Controller
@RequestMapping("/domain")
public class DomainInfoController {
	@Resource
	private IDomainInfoService domainInfoService;

	private static final Logger log = LoggerFactory
			.getLogger(DomainInfoController.class);

	/**
	 * 插入区域
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/insertDomain", method = RequestMethod.POST)
	@ResponseBody
	public Response<DomainInfoDto> insertDomain(
			@RequestBody DomainInfoDto domainInfoDto) {
		Response<DomainInfoDto> response = new Response<DomainInfoDto>();
		if (null != domainInfoDto) {
			DomainInfo domain = new DomainInfo();
			BeanUtils.copyProperties(domainInfoDto, domain);
			domain.setLatitude(domainInfoDto.getLatitude().doubleValue());
			domain.setLongitude(domainInfoDto.getLongitude().doubleValue());
			domain.setRadius(domainInfoDto.getRadius().doubleValue());
			if (null == domain.getParentId()) {
				domain.setParentId(0L);
				domain.setPath("0");
			} else {
				DomainInfo d = domainInfoService.selectDomainById(domain
						.getParentId());
				domain.setPath(d.getPath() + "@" + domain.getParentId());
			}
			Integer result = domainInfoService.insertDomain(domain);
			if (null != result && result.equals(1)) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("insert domain success, domain name is "
						+ domainInfoDto.getName());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("insert domain fail, domain name is "
						+ domainInfoDto.getName());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("insert domain fail, domain data is null");
		}

		return response;
	}

	/**
	 * 根据id更新区域
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/updateDomain", method = RequestMethod.POST)
	@ResponseBody
	public Response<DomainInfoDto> updateDomain(
			@RequestBody DomainInfoDto domainInfoDto) {
		Response<DomainInfoDto> response = new Response<DomainInfoDto>();
		if (null != domainInfoDto && null != domainInfoDto.getId()) {
			DomainInfo domain = new DomainInfo();
			BeanUtils.copyProperties(domainInfoDto, domain);
			domain.setLatitude(domainInfoDto.getLatitude().doubleValue());
			domain.setLongitude(domainInfoDto.getLongitude().doubleValue());
			domain.setRadius(domainInfoDto.getRadius().doubleValue());
			
			DomainInfo parentDomain = domainInfoService.selectDomainById(domainInfoDto.getParentId());
			if (null != parentDomain) {
				domain.setPath(parentDomain.getPath()+"@"+parentDomain.getId());
			}else {
				domain.setPath("0");
			}
			
			Integer result = domainInfoService.updateDomain(domain);
			if (null != result && result.equals(1)) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("update domain success, domain id is "
						+ domainInfoDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("update domain fail, domain id is "
						+ domainInfoDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("update domain fail, domain id is null");
		}
		return response;
	}

	/**
	 * 根据id删除区域
	 */
	@RequestMapping(value = "/deleteDomainById", method = RequestMethod.POST)
	@ResponseBody
	public Response<DomainInfoDto> deleteDomainById(
			@RequestBody DomainInfoDto domainInfoDto, HttpServletRequest request) {
		Response<DomainInfoDto> response = new Response<DomainInfoDto>();
		if (null != domainInfoDto && null != domainInfoDto.getId()) {
			// 判断区域下是否有子区域
			Integer count = domainInfoService
					.countDomainByParentId(domainInfoDto.getId());
			if (count > 0) {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "domain.del.failed.hasChildren")); // 删除失败,需要删除该区域下面的所有子区域以后才能删除该区域!
				log.error("delete domain fail, domain has some sub domain");
				return response;
			}
			// 判断区域下是否有电站
			Integer stationCount = domainInfoService.countStationByDomainId(domainInfoDto.getId());
			if( stationCount > 0)
			{
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "domain.del.failed.hasStation")); // 删除失败,需要先删除区域绑定的电站才能删除该区域
				log.error("delete domain fail, domain has some sub domain");
				return response;
			}
			Integer result = domainInfoService.deleteDomainById(domainInfoDto
					.getId());
			if (null != result && result.equals(1)) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("delete domain success, domain id is "
						+ domainInfoDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "domain.del.failed")); // 删除失败
				log.error("delete domain fail, domain id is "
						+ domainInfoDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "domain.del.failed")); // 删除失败
			log.error("delete domain fail, domain id is null");
		}

		return response;
	}

	/**
	 * 根据id查询区域
	 */
	@RequestMapping(value = "/getDomainById", method = RequestMethod.POST)
	@ResponseBody
	public Response<DomainInfo> getDomainById(
			@RequestBody DomainInfoDto domainInfoDto) {
		Response<DomainInfo> response = new Response<>();
		if (null != domainInfoDto && null != domainInfoDto.getId()) {
			DomainInfo domain = domainInfoService
					.selectDomainById(domainInfoDto.getId());
			if (null != domain) {
				if (domain.getParentId() != 0 && null != domain.getParentId()) {
					DomainInfo parent = domainInfoService
							.selectDomainById(domain.getParentId());
					if (null != parent) {
						domain.setParentName(parent.getName());
					}
				} else {
					domain.setParentName(null);
				}
				// TreeModel model = changeTOTreeModel(domain);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(domain);
				log.info("get domain by id success, domain id is "
						+ domainInfoDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("get domain by id fail, domain id is "
						+ domainInfoDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("get domain by id fail, domain id is null");
		}

		return response;
	}

	private TreeModel changeTOTreeModel(DomainInfo domain) {
		TreeModel model = new TreeModel();
		model.setId(domain.getId() + "");
		model.setName(domain.getName());
		model.setText(domain.getDescription());
		model.setPid(domain.getParentId() + "");
		model.setPath(domain.getPath());
		return model;
	}

	@RequestMapping(value = "/editDomainTree", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<TreeModel>> editDomainTree(
			@RequestBody DomainInfoDto domainInfoDto, HttpSession session) {
		Response<List<TreeModel>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (null != domainInfoDto && null != domainInfoDto.getModel()
				&& null != domainInfoDto.getId() && null != obj) {
			if (domainInfoDto.getModel().equals("E")) {
				// 根据企业id查询所有子区域
				List<DomainInfo> result = domainInfoService
						.selectTreeByEnterpriseId(domainInfoDto.getId());
				if (null != result) {
					List<TreeModel> list = new ArrayList<>();
					for (DomainInfo domain : result) {
						if (!domain.getId().equals(domainInfoDto.getId())) {
							list.add(changeTOTreeModel(domain));
						}
					}
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					response.setResults(list);
					log.info("query domain by id success, domain id or enterprise id is "
							+ domainInfoDto.getId());
				} else {
					response.setCode(ResponseConstants.CODE_FAILED);
					response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
					log.error("query domain by id fail, domain id or enterprise id is "
							+ domainInfoDto.getId());
				}
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query domain by id fail, domain id or enterprise id is null");
		}
		return response;
	}

	/**
	 * 根据企业id或者区域id查询所有的区域
	 */
	@RequestMapping(value = "/getDomainTree", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<TreeModel>> getAllDomain(
			@RequestBody DomainInfoDto domainInfoDto, HttpSession session) {
		Response<List<TreeModel>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (null != domainInfoDto && null != domainInfoDto.getModel()
				&& null != domainInfoDto.getId() && null != obj) {
			List<DomainInfo> result = null;
			if (domainInfoDto.getModel().equals("D")) {
				// 根据区域的父id查询所有子区域
				result = domainInfoService.selectTreeByParentId(domainInfoDto.getId());
			} else if (domainInfoDto.getModel().equals("E")) {
				// 根据企业id查询所有子区域
				result = domainInfoService
						.selectTreeByEnterpriseId(domainInfoDto.getId());
			}
			if (null != result) {
				List<TreeModel> list = new ArrayList<>();
				for (DomainInfo domain : result) {
					list.add(changeTOTreeModel(domain));
				}
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(list);
				log.info("query domain by id success, domain id or enterprise id is "
						+ domainInfoDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("query domain by id fail, domain id or enterprise id is "
						+ domainInfoDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query domain by id fail, domain id or enterprise id is null");
		}

		return response;
	}
	
	@RequestMapping(value = "/getDomainTree1", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<DomainInfo>> getDomainTree1(@RequestBody DomainInfoDto domainInfoDto)
	{
		Response<List<DomainInfo>> response = new Response<>();
		if(null != domainInfoDto && null != domainInfoDto.getId() && null != domainInfoDto.getModel())
		{
			List<DomainInfo> list = domainInfoService.getTree(domainInfoDto.getId(),domainInfoDto.getModel());
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(list);
			log.info("query domain tree success");
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query domain tree fail, no id or no model");
		}
		return response;
	}

	/**
	 * 根据父id查询所有的子区域 - 查询树形结构的时候使用
	 */
	@RequestMapping(value = "/getDomainsByParentIdForTree", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<TreeModel>> getDomainsByParentIdForTree(
			HttpServletRequest request, HttpSession session) {
		Response<List<TreeModel>> response = new Response<>();
		Object obj = session.getAttribute("user");
		String id = request.getParameter("id");
		String model = request.getParameter("model");

		DomainInfoDto domainInfoDto = new DomainInfoDto();
		domainInfoDto.setId(id != null ? Long.parseLong(id) : null);
		domainInfoDto.setModel(model);

		getDomain(domainInfoDto, response, obj);
		return response;

	}

	/**
	 * 根据父id查询所有的子区域
	 */
	@RequestMapping(value = "/getDomainsByParentId", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<TreeModel>> getDomainsByParentId(
			@RequestBody DomainInfoDto domainInfoDto, HttpSession session) {
		Response<List<TreeModel>> response = new Response<>();
		Object obj = session.getAttribute("user");
		getDomain(domainInfoDto, response, obj);

		return response;
	}

	private void getDomain(DomainInfoDto domainInfoDto,
			Response<List<TreeModel>> response, Object obj) {
		if (null != domainInfoDto && null != domainInfoDto.getModel()
				&& null != domainInfoDto.getId() && null != obj) {
			List<DomainInfo> result = null;
			if (domainInfoDto.getModel().equals("D")) {
				// 根据区域的父id查询直接子区域
				result = domainInfoService.selectDomainsByParentId(domainInfoDto.getId());
			} else if (domainInfoDto.getModel().equals("E")) {
				// 根据企业id查询直接子区域
				result = domainInfoService.selectDomainsByEnter(domainInfoDto
						.getId());
			}
			if (null != result) {
				List<TreeModel> list = new ArrayList<>();
				TreeModel model = null;
				Integer count = 0;
				for (DomainInfo domain : result) {
					model = changeTOTreeModel(domain);
					count = domainInfoService.countDomainByParentId(domain
							.getId());
					if(count > 0)
					{
						model.setLeaf(false);
					}else{
						model.setLeaf(true);
					}
					list.add(model);
				}
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(list);
				log.info("query domain by parentId or enterpriseId success, domain id or enterprise id is "
						+ domainInfoDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("query domain by parentId or enterpriseId fail, domain id or enterprise id is "
						+ domainInfoDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query domain by parentId or enterpriseId fail, domain id or enterprise id is null");
		}
	}

	/**
	 * 根据电站编码查询区域信息
	 * 
	 * @param stationCode
	 * @return
	 */
	@RequestMapping(value = "/getDomainByStationCode", method = RequestMethod.POST)
	@ResponseBody
	public Response<DomainInfoDto> getDomainByStationCode(
			@RequestBody DomainInfoDto domainInfoDto) {
		Response<DomainInfoDto> response = new Response<>();
		if (null != domainInfoDto && null != domainInfoDto.getStationCode()) {
			DomainInfo result = domainInfoService
					.selectDomainByStationCode(domainInfoDto.getStationCode());
			if (null != result) {
				BeanUtils.copyProperties(result, domainInfoDto);
				response.setResults(domainInfoDto);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}

}
